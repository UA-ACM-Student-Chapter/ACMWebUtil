package edu.ua.cs.acm.controllers;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.domain.Semester;
import edu.ua.cs.acm.services.MemberService;
import edu.ua.cs.acm.services.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}