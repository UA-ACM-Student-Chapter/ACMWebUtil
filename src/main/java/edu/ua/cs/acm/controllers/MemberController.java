package edu.ua.cs.acm.controllers;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.domain.Semester;
import edu.ua.cs.acm.repositories.MemberRepository;
import edu.ua.cs.acm.repositories.SemesterRepository;
import edu.ua.cs.acm.services.MemberService;
import edu.ua.cs.acm.services.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

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

    @GetMapping()
    public ResponseEntity<Member> getMember() {
        Member m = new Member("Jake", "Zarobsky", "L",
                LocalDateTime.of(1996, 7, 5, 0, 0), "jazarobsky@crimson.ua.edu");

        Semester s = semesterService.getCurrentSemester();

        try {
            semesterService.saveSemester(s);

            m.getSemesters().add(s);


            m = memberService.save(m);

            s.getMembers().add(m);

            semesterService.saveSemester(s);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<Member>(m, HttpStatus.OK);
    }

}
