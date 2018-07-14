package edu.ua.cs.acm.controllers;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.domain.MemberSemesterLink;
import edu.ua.cs.acm.domain.Semester;
import edu.ua.cs.acm.messages.IsPaidMessage;
import edu.ua.cs.acm.messages.UpdateShirtSizeMessage;
import edu.ua.cs.acm.messages.PayForSemesterMessage;
import edu.ua.cs.acm.services.CommonService;
import edu.ua.cs.acm.services.MemberService;
import edu.ua.cs.acm.services.SemesterService;
import edu.ua.cs.acm.services.EmailService;
import edu.ua.cs.acm.email.PaymentConfirmationEmailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by jzarobsky on 11/21/17.
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final SemesterService semesterService;
    private final EmailService emailService;
    private final CommonService commonService;

    @Autowired
    public MemberController(MemberService memberService, SemesterService semesterService, EmailService emailService, CommonService commonService) {
        this.memberService = memberService;
        this.semesterService = semesterService;
        this.emailService = emailService;
        this.commonService = commonService;
    }

    @CrossOrigin
    @GetMapping("/wakeup")
    public ResponseEntity wakeup() {
        return ResponseEntity.ok("awake");
    }

    @GetMapping("/unpaid")
    public ResponseEntity<Object> unpaidMembers(@RequestHeader String secretKey) {
        if (secretKey.equals(System.getenv("SECRET_KEY"))) {
            return ResponseEntity.ok(memberService.unpaidMembers(semesterService.getCurrentSemester()));
        }
        return ResponseEntity.ok("no secret key, no secret knowledge");
    }

    @GetMapping("/all")
    public ResponseEntity<Object> allMembers(@RequestHeader String secretKey) {
        if (secretKey.equals(System.getenv("SECRET_KEY"))) {
            return ResponseEntity.ok(memberService.allMembers());
        }
        return ResponseEntity.ok("no secret key, no secret knowledge");
    }

    @CrossOrigin
    @PostMapping("/checkMemberForDues")
    public ResponseEntity<Object> checkMemberForDues(@RequestBody PayForSemesterMessage message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        Semester currentSemester = semesterService.getCurrentSemester();
        if (currentSemester == null) {
            return commonService.createResponse("There is no active semester to pay for right now. Contact acm-off@listserv.ua.edu for questions.", response);
        }
        Member member = memberService.getByCrimsonEmail(message.getEmail());
        if (member == null) {
            return commonService.createResponse(member.getCrimsonEmail() + " is not linked to an existing member. Please join before paying dues.", response);
        }
        if (currentSemester.getMembers().contains(member)) {
            return commonService.createResponse((member.getCrimsonEmail() + " has already paid for the current semester."), response);
        }
        response.put("success", true);
        return commonService.createResponse("", response);
    }

    @PostMapping("/updateshirtsize")
    public ResponseEntity<Object> updateShirtSize(@RequestBody UpdateShirtSizeMessage message) throws URISyntaxException {
        if (message.getSecretKey().equals(System.getenv("SECRET_KEY"))) {
            Member memberToUpdate = memberService.getByCrimsonEmail(message.getEmail());

            if (memberToUpdate != null) {
                memberService.updateShirtSize(memberToUpdate, message.getNewShirtSize());
            }

            return ResponseEntity.ok("updated");
        }
        return ResponseEntity.ok("no secret key, no secret knowledge");
    }

    @PostMapping("/ispaid")
    public Object memberIsPaid(@RequestBody IsPaidMessage message) {
        if (message.getSecretKey().equals(System.getenv("SECRET_KEY"))) {
            Member m = memberService.getByCrimsonEmail(message.getEmail());
            Integer paidMember = null;

            if (m != null) {
                paidMember = semesterService.memberIsPaid(m);
            } else {
                return ResponseEntity.ok("member not found");
            }
            if (paidMember == null) {
                return ResponseEntity.ok("not paid");
            }
            else {
                return ResponseEntity.ok("paid");
            }
        }
        return "no secret key, no secret knowledge";
    }

    @PostMapping("/payforsemester")
    public ResponseEntity<Object> payForSemester(@RequestBody PayForSemesterMessage message) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://ua-acm-web-payments.herokuapp.com/validate";
        String requestJson = "{\"id\":\"" + message.getPurchaseID() + "\", \"email\":\"" + message.getEmail() + "\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        String response = restTemplate.postForObject(url, entity, String.class);

        if (response != "no" && response.equals(message.getDatePaid())) {

            Member payingMember = memberService.getByCrimsonEmail(message.getEmail());
            int semesterId = semesterService.currentSemesterId();
            String paymentType = "Other";
            String ccNumber = "N/A";

            if (payingMember != null) {
                memberService.payForSemester(payingMember, semesterId, message.getPurchaseID());
                memberService.updateShirtSize(payingMember, message.getSize());
                if (message.getPaymentType().equals("credit_card")) {
                    paymentType = "Credit Card";
                    ccNumber = "************" + message.getLast4();
                }
                else if (message.getPaymentType().equals("venmo_account")){
                    paymentType = "Venmo";
                    ccNumber = message.getLast4();
                }
                System.out.println(message.getPaymentType());
                emailService.sendMessage(new PaymentConfirmationEmailMessage(payingMember.getFirstName(), payingMember.getLastName(), payingMember.getCrimsonEmail(), message.getDatePaid(), "$10", paymentType, message.getPurchaseID(), ccNumber, message.getCardType()));
                return new ResponseEntity<>("{\"id\":\"" + message.getPurchaseID() + "\", \"email\":\"" + message.getEmail() + "\", \"name\":\"" + payingMember.getFirstName() + " " + payingMember.getLastName() + "\", \"paymentType\":\"" + paymentType + "\", \"cardType\":\"" + message.getCardType() + "\", \"hiddenCCNumber\":\"" + ccNumber + "\", \"date\": \"" + message.getDatePaid() + "\"}", HttpStatus.OK);
            }
            return new ResponseEntity<>("{\"noUser\": \"true\"}", HttpStatus.OK);
        }
        else return new ResponseEntity<>("{\"notValid\": \"true\"}", HttpStatus.OK);
    }

}
