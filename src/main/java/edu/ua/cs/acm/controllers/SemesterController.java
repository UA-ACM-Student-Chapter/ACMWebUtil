package edu.ua.cs.acm.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ua.cs.acm.domain.Semester;
import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.email.DueReminderEmailMessage;
import edu.ua.cs.acm.messages.AddSemesterMessage;
import edu.ua.cs.acm.services.CommonService;
import edu.ua.cs.acm.services.EmailService;
import edu.ua.cs.acm.services.SemesterService;
import edu.ua.cs.acm.services.MemberService;
import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by jzarobsky on 11/21/17.
 */
@RestController
@RequestMapping("/semester")
public class SemesterController {

    private static final Gson objGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    private final SemesterService semesterService;
    private final MemberService memberService;
    private final EmailService emailService;
    private final CommonService commonService;

    @Autowired
    public SemesterController(SemesterService semesterService, MemberService memberService, EmailService emailService, CommonService commonService) {
        this.semesterService = semesterService;
        this.memberService = memberService;
        this.emailService = emailService;
        this.commonService = commonService;
    }

    @GetMapping("/getCurrentSemester")
    public ResponseEntity<Object> getCurrentSemester(@RequestHeader String secretKey) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        if (commonService.validateSecret(secretKey)) {
            response.put("data",  semesterService.getCurrentSemester());
            response.put("success", true);
            return commonService.createResponse("", response);
        }
        return commonService.createResponse("No secret key, no secret knowledge", response);
    }

    @RequestMapping(value = "/scheduledreminder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public HttpEntity scheduledReminder(@RequestParam Integer scheduled_day, @RequestParam String secretKey) {
        if (commonService.validateSecret(secretKey)) {
            Calendar c = Calendar.getInstance();
            Integer dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            if (scheduled_day == dayOfWeek && LocalDateTime.now().isBefore(semesterService.currentDueDate())) {
                List<Member> unpaidMembers = memberService.unpaidMembers(semesterService.getCurrentSemester());
                for (Member m: unpaidMembers) {
                    emailService.sendMessage(new DueReminderEmailMessage(m.getFirstName(), m.getCrimsonEmail(), semesterService.currentDueDate()));
                }
                return new HttpEntity("ok");
            }
            return new HttpEntity("nothing sent; past due date");
        }
        return new HttpEntity("Wrong secret key");
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addSemester(@RequestBody AddSemesterMessage request) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        if (commonService.validateSecret(request.getSecretKey())) {
            LocalDateTime startDate = LocalDate.parse(request.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(request.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            LocalDateTime dueDate = LocalDate.parse(request.getDueDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            Semester newSemester = new Semester(startDate, endDate, dueDate);
            try {
                semesterService.saveSemester(newSemester);
            }
            catch (Exception ex){
                return commonService.createResponse("Semester could not be added", response);
            }
            response.put("success", true);
            return commonService.createResponse("", response);
        }
        return commonService.createResponse("Bad secret key", response);
    }

    @CrossOrigin
    @GetMapping("/report")
    public ResponseEntity<Object> generateReport(@RequestHeader String secretKey) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        if (commonService.validateSecret(secretKey)) {
            Semester currentSemester;
            try {
                currentSemester = semesterService.getCurrentSemester();
                response.put("semester", currentSemester);
            }
            catch (Exception ex) {
                return commonService.createResponse("Could not get the current semester", response);
            }
            if (currentSemester != null) {
                List<Member> paidMembers;
                try {
                    paidMembers = memberService.paidMembers(currentSemester);
                    response.put("paid", paidMembers);
                }
                catch (Exception ex) {
                    return commonService.createResponse("Could not get the list of paid members for the semester", response);
                }
                List<Member> unpaidMembers;
                try {
                    unpaidMembers = memberService.unpaidMembers(currentSemester);
                    response.put("unpaid", unpaidMembers);
                }
                catch (Exception ex) {
                    return commonService.createResponse("Could not get the list of unpaid members for the semester", response);
                }
                response.put("semesterName", System.getenv("CURRENT_SEMESTER_NAME"));
                Map<String, Object> shirtsNeeded = new HashMap<>();
                int xsCount = (int) paidMembers.stream().filter(member -> member.getShirtSize().equals("XS")).count();
                shirtsNeeded.put("XS", xsCount);
                int sCount = (int) paidMembers.stream().filter(member -> member.getShirtSize().equals("S")).count();
                shirtsNeeded.put("S", sCount);
                int mCount = (int) paidMembers.stream().filter(member -> member.getShirtSize().equals("M")).count();
                shirtsNeeded.put("M", mCount);
                int lCount = (int) paidMembers.stream().filter(member -> member.getShirtSize().equals("L")).count();
                shirtsNeeded.put("L", lCount);
                int xlCount = (int) paidMembers.stream().filter(member -> member.getShirtSize().equals("XL")).count();
                shirtsNeeded.put("XL", xlCount);
                int xxlCount = (int) paidMembers.stream().filter(member -> member.getShirtSize().equals("XXL")).count();
                shirtsNeeded.put("XXL", xxlCount);
                int xxxlCount = (int) paidMembers.stream().filter(member -> member.getShirtSize().equals("XXXL")).count();
                shirtsNeeded.put("XXXL", xxxlCount);
                response.put("shirtsNeeded", shirtsNeeded);
                response.put("success", true);
                return commonService.createResponse("", response);
            }
            return commonService.createResponse("There is no current semester", response);
        }
        return commonService.createResponse("Bad secret key", response);
    }

}
