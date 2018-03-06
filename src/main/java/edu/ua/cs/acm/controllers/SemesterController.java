package edu.ua.cs.acm.controllers;

import edu.ua.cs.acm.domain.Semester;
import edu.ua.cs.acm.domain.Member;
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
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by jzarobsky on 11/21/17.
 */
@RestController
@RequestMapping("/semester")
public class SemesterController {

    private final SemesterService semesterService;
    private final MemberService memberService;
    private final String authKey;

    @Autowired
    public SemesterController(SemesterService semesterService, MemberService memberService, @Value("${authorization-key}") String authKey) {
        this.semesterService = semesterService;
        this.memberService = memberService;
        this.authKey = authKey;
    }

    @GetMapping()
    public ResponseEntity<Semester> getSemester() {
        return ResponseEntity.ok(semesterService.getCurrentSemester());
    }

    @PostMapping()
    public ResponseEntity<Semester> createSemester(@RequestBody Semester semester,
                                                   @RequestHeader(name="Authorization", required = true) String authorization) {

        if(!authKey.equals(authorization)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            semesterService.saveSemester(semester);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(semester);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Semester> updateSemester(@RequestBody Semester semester,
                                                   @PathVariable("id") int id,
                                                   @RequestHeader(name="Authorization", required=true) String authorization
    ) {
        if(!authKey.equals(authorization)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        semester.setId(id);
        try {
            semesterService.saveSemester(semester);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(semester);
    }

    @RequestMapping(value = "/scheduledreminder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public HttpEntity scheduledReminder(@RequestParam Integer scheduled_day, @RequestParam String secret_key) {
        System.out.println("got a schedule request");
        if (secret_key.equals(System.getenv().get("SECRET_KEY"))) {
            Calendar c = Calendar.getInstance();
            Integer dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            if (scheduled_day == dayOfWeek && LocalDateTime.now().isBefore(semesterService.currentDueDate())) {
                List<Member> unpaidMembers = memberService.unpaidMembers(semesterService.getCurrentSemester());
                String response = "Going to send an email to:";
                for (Member m: unpaidMembers) {
                    response += " " + m.getCrimsonEmail();
                }
                return new HttpEntity(response);
            }
        }

        return new HttpEntity("nothing sent; past due date");
    }

    @GetMapping("/unpaiddetails")
    public Map<String, Object> unpaidDetails() {
        HashMap<String, Object> response = new HashMap<>();
        response.put("dueDate", semesterService.currentDueDate());
        response.put("unpaidMembers", memberService.unpaidMembers(semesterService.getCurrentSemester()));
        return response;
    }

}
