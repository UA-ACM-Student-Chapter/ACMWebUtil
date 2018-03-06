package edu.ua.cs.acm.controllers;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.domain.Semester;
import edu.ua.cs.acm.messages.IsPaidMessage;
import edu.ua.cs.acm.messages.UpdateShirtSizeMessage;
import edu.ua.cs.acm.messages.PayForSemesterMessage;
import edu.ua.cs.acm.services.MemberService;
import edu.ua.cs.acm.services.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by jzarobsky on 11/21/17.
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final SemesterService semesterService;

    @Autowired
    public MemberController(MemberService memberService, SemesterService semesterService) {
        this.memberService = memberService;
        this.semesterService = semesterService;
    }

    @DeleteMapping()
    public ResponseEntity deleteMember(@RequestParam("email") String email) {
        Member m = memberService.getByCrimsonEmail(email);

        if(m != null) {
            memberService.delete(m);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<Member>> unpaidMembers() {
        return ResponseEntity.ok(memberService.unpaidMembers(semesterService.getCurrentSemester()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Member>> allMembers() {
        return ResponseEntity.ok(memberService.allMembers());
    }

    @PostMapping("/makepayment")
    public ResponseEntity payment() {
        System.out.println("Got a payment request " + System.currentTimeMillis());
        //TODO Something useful
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateshirtsize")
    public ResponseEntity<Object> updateShirtSize(@RequestBody UpdateShirtSizeMessage message) throws URISyntaxException {
        Member memberToUpdate = memberService.getByCrimsonEmail(message.getEmail());

        if (memberToUpdate != null) {
            memberService.updateShirtSize(memberToUpdate, message.getNewShirtSize());
        }

        // redirect the user to a success page
        URI success = new URI("http://www.UA-ACM-Student-Chapter.github.io/update-shirt-size/success.html");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(success);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @PostMapping("/ispaid")
    public ResponseEntity memberIsPaid(@RequestBody IsPaidMessage message) {
        Member m = memberService.getByCrimsonEmail(message.getEmail());
        int paidMember = -1;

        if (m != null) {
            paidMember = semesterService.memberIsPaid(m);
        } else {
            return ResponseEntity.ok("member not found");
        }

        if (m.getId() == paidMember) {
            return ResponseEntity.ok("paid");
        } else {
            return ResponseEntity.ok("not paid");
        }
    }

    @PostMapping("/payforsemester")
    public ResponseEntity payForSemester(@RequestBody PayForSemesterMessage message) {
        Member payingMember = memberService.getByCrimsonEmail(message.getEmail());
        int semesterId = semesterService.currentSemesterId();

        if (payingMember != null) {
            memberService.payForSemester(payingMember, semesterId, message.getPurchaseID());
        }

        return ResponseEntity.ok("success");
    }

}
