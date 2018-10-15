package edu.ua.cs.acm.controllers;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.domain.Semester;
import edu.ua.cs.acm.email.ListservCommand;
import edu.ua.cs.acm.email.ListservUnsubscribe;
import edu.ua.cs.acm.messages.IsPaidMessage;
import edu.ua.cs.acm.messages.UpdateShirtSizeMessage;
import edu.ua.cs.acm.messages.PayForSemesterMessage;
import edu.ua.cs.acm.services.CommonService;
import edu.ua.cs.acm.services.MemberService;
import edu.ua.cs.acm.services.SemesterService;
import edu.ua.cs.acm.services.EmailService;
import edu.ua.cs.acm.email.PaymentConfirmationEmailMessage;
import edu.ua.cs.acm.services.impl.MemberServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jzarobsky on 11/21/17.
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    private static final Logger LOG = LoggerFactory.getLogger(JoinController.class);
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
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        if (commonService.validateSecret(secretKey)) {
            List<Member> unpaidMembers = memberService.unpaidMembers(semesterService.getCurrentSemester());
            if (unpaidMembers != null) {
                response.put("unpaid", unpaidMembers);
                response.put("success", true);
                return commonService.createResponse("", response);
            }
            return commonService.createResponse("There is no active semester, so no member is considered unpaid", response);
        }
        return commonService.createResponse("Wrong secret key", response);
    }

    @GetMapping("/paid")
    public ResponseEntity<Object> paidMembers(@RequestHeader String secretKey) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        if (commonService.validateSecret(secretKey)) {
            List<Member> paidMembers = memberService.paidMembers(semesterService.getCurrentSemester());
            if (paidMembers != null) {
                response.put("paid", paidMembers);
                response.put("success", true);
                return commonService.createResponse("", response);
            }
            return commonService.createResponse("There is no active semester, so no member is considered paid", response);
        }
        return commonService.createResponse("Wrong secret key", response);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> allMembers(@RequestHeader String secretKey) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        if (commonService.validateSecret(secretKey)) {
            response.put("all", memberService.allMembers());
            response.put("success", true);
            return commonService.createResponse("", response);
        }
        return commonService.createResponse("Wrong secret key", response);
    }

    @CrossOrigin
    @PostMapping("/checkMemberForDues")
    public ResponseEntity<Object> checkMemberForDues(@RequestBody PayForSemesterMessage message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        Semester currentSemester = semesterService.getCurrentSemester();
        if (currentSemester == null) {
            return commonService.createResponse("There is no active semester to pay for right now. Contact " + System.getenv("OFFICERS_EMAIL") + " for questions.", response);
        }
        Member member = memberService.getByCrimsonEmail(message.getEmail());
        if (member == null) {
            return commonService.createResponse(message.getEmail() + " is not linked to an existing member. Please use the Join button on the homepage before paying dues.", response);
        }
        if (currentSemester.getMembers().contains(member)) {
            return commonService.createResponse((member.getCrimsonEmail() + " has already paid for the current semester."), response);
        }
        response.put("success", true);
        return commonService.createResponse("", response);
    }

    @PostMapping("/updateshirtsize")
    public ResponseEntity<Object> updateShirtSize(@RequestBody UpdateShirtSizeMessage message) throws URISyntaxException {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        if (commonService.validateSecret(message.getSecretKey())) {
            Member memberToUpdate = memberService.getByCrimsonEmail(message.getEmail());
            if (memberToUpdate != null) {
                memberService.updateShirtSize(memberToUpdate, message.getNewShirtSize());
                response.put("success", true);
                return commonService.createResponse("", response);
            }
            return commonService.createResponse(message.getEmail() + " was not found.", response);
        }
        return commonService.createResponse("Wrong secret key", response);
    }

    @PostMapping("/ispaid")
    public Object memberIsPaid(@RequestBody IsPaidMessage message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        if (commonService.validateSecret(message.getSecretKey())) {
            Member member = memberService.getByCrimsonEmail(message.getEmail());
            boolean memberHasPaid;

            if (member != null) {
                memberHasPaid = semesterService.memberIsPaid(member) != null;
            } else {
                return commonService.createResponse(message.getEmail() + " was not found", response);
            }
            if (!memberHasPaid) {
                response.put("success", true);
                response.put("hasPaid", false);
                return commonService.createResponse("", response);
            }
            response.put("success", true);
            response.put("hasPaid", true);
            return commonService.createResponse("", response);
        }
        return commonService.createResponse("Wrong secret key", response);
    }

    @PostMapping("/payforsemester")
    public ResponseEntity<Object> payForSemester(@RequestBody PayForSemesterMessage message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://" + System.getenv("PAYMENTS_INSTANCE_NAME") + ".herokuapp.com/validate";
        String requestJson = "{\"id\":\"" + message.getPurchaseID() + "\", \"email\":\"" + message.getEmail() + "\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestJson,headers);
        String validateResponse = restTemplate.postForObject(url, entity, String.class);

        if (validateResponse != "no" && validateResponse.equals(message.getDatePaid())) {

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

                try {
                    emailService.sendMessage(new PaymentConfirmationEmailMessage(payingMember.getFirstName(), payingMember.getLastName(), payingMember.getCrimsonEmail(), LocalDateTime.now().toLocalDate().toString(), "$10", paymentType, message.getPurchaseID(), ccNumber, message.getCardType()));
                }
                catch (Exception ex) {
                    LOG.error(ex.getMessage());
                    LOG.error("Email confirmation for payment was unable to be sent for " + payingMember.toString());
                }

                response.put("id", message.getPurchaseID());
                response.put("email", message.getEmail());
                response.put("name", payingMember.getFirstName() + " " + payingMember.getLastName());
                response.put("paymentType", paymentType);
                response.put("cardType", message.getCardType());
                response.put("hiddenCCNumber", ccNumber);
                response.put("date", LocalDateTime.now().toLocalDate().toString());
                response.put("success", true);
                return commonService.createResponse("", response);
            }
            return commonService.createResponse("No user matching " + message.getEmail() + " was found.", response);
        }
        return commonService.createResponse("The provided payment information was not determined to be valid.", response);
    }

    @PostMapping("/emailunsubscribe")
    public boolean listservUnsubscribe(@RequestBody String email) {
        Member user = memberService.getByCrimsonEmail(email);
        if (user != null) {
            try {
                emailService.sendMessage(new ListservUnsubscribe(email));
            } catch (Exception e) {
                LOG.error(e.getMessage());
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
