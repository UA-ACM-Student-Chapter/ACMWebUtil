package edu.ua.cs.acm.controllers;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.email.JoinEmailMessage;
import edu.ua.cs.acm.messages.JoinMessage;
import edu.ua.cs.acm.services.EmailService;
import edu.ua.cs.acm.services.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jzarobsky on 9/4/17.
 */
@RestController
@RequestMapping("/join")
public class JoinController {

    private static final Logger LOG = LoggerFactory.getLogger(JoinController.class);
    private final EmailService emailService;
    private final MemberService memberService;

    public JoinController(EmailService emailService, MemberService memberService) {
        this.emailService = emailService;
        this.memberService = memberService;
    }

    @CrossOrigin
    @PostMapping()
    public String joinAcm(@RequestBody JoinMessage message) {

        try {
            Member m = new Member(message.getFirstName(), message.getLastName(), message.getShirtSize(),
                    message.getBirthday(), message.getEmail());

            memberService.save(m);

            emailService.sendMessage(new JoinEmailMessage(message.getFirstName(), message.getLastName(),
                    message.getEmail(), message.wantsSlackToJoinSlack()));
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            return null;
        }

        return "ok";
    }

}
