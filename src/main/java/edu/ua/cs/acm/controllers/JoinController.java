package edu.ua.cs.acm.controllers;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.email.JoinEmailMessage;
import edu.ua.cs.acm.email.ListservCommand;
import edu.ua.cs.acm.messages.JoinMessage;
import edu.ua.cs.acm.services.EmailService;
import edu.ua.cs.acm.services.MemberService;
import edu.ua.cs.acm.services.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by jzarobsky on 9/4/17.
 */
@RestController
@RequestMapping("/join")
public class JoinController {

    private static final Logger LOG = LoggerFactory.getLogger(JoinController.class);
    private final EmailService emailService;
    private final MemberService memberService;
    private final CommonService commonService;

    public JoinController(EmailService emailService, MemberService memberService, CommonService commonService) {
        this.emailService = emailService;
        this.memberService = memberService;
        this.commonService = commonService;
    }

    private boolean memberIsAlreadyAdded(String email) {
        return memberService.getByCrimsonEmail(email) != null;
    }

    private boolean addMemberToDb(JoinMessage message) {
        try {
            Member member = new Member(message.getFirstName(), message.getLastName(), message.getShirtSize(),
                    message.getBirthday(), message.getEmail());
            memberService.save(member);
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            return false;
        }
        return true;
    }

    private boolean sendWelcomeEmail(JoinMessage message) {
        try {
            emailService.sendMessage(new JoinEmailMessage(message.getFirstName(), message.getLastName(),
                    message.getEmail(), message.wantsSlackToJoinSlack()));
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage());
            return false;
        }
        return true;
    }

    private boolean sendListservAddCommand(JoinMessage message) {
        try {
            emailService.sendMessage(new ListservCommand(message.getFirstName(), message.getLastName(),
                    message.getEmail(), "ADD"));
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage());
            return false;
        }
        return true;
    }

    @CrossOrigin
    @PostMapping()
    public ResponseEntity<Object> joinAcm(@RequestBody JoinMessage message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        if (memberIsAlreadyAdded(message.getEmail())) {
            return commonService.createResponse(message.getEmail() + "is already a member", response);
        }
        if (!addMemberToDb(message)) {
            return commonService.createResponse("The member could not be saved. Email acm-off@listserv.ua.edu for help.", response);
        }
        if (!sendWelcomeEmail(message)) {
            return commonService.createResponse("There was an error sending the welcome email. Email acm-off@listserv.ua.edu for help.", response);
        }
        if (!sendListservAddCommand(message)) {
            return commonService.createResponse("There was an error adding the member to the mailing list. Email acm-off@listserv.ua.edu for help.", response);
        }
        response.put("success", true);
        return commonService.createResponse("", response);
    }

}
