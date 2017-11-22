package edu.ua.cs.acm.controllers;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.domain.Semester;
import edu.ua.cs.acm.repositories.MemberRepository;
import edu.ua.cs.acm.repositories.SemesterRepository;
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

    private final MemberRepository memberRepository;
    private final SemesterRepository semesterRepository;

    @Autowired
    public MemberController(MemberRepository memberRepository, SemesterRepository semesterRepository) {
        this.memberRepository = memberRepository;
        this.semesterRepository = semesterRepository;
    }

    @GetMapping()
    public ResponseEntity<Member> getMember() {
        Member m = new Member("Jake", "Zarobsky", "L",
                LocalDateTime.of(1996, 7, 5, 0, 0), "jazarobsky@crimson.ua.edu");

        Semester s = new Semester(LocalDateTime.of(2017, 8, 1, 0,0),
                LocalDateTime.of(2017, 12, 31, 23, 59));

        semesterRepository.save(s);

        m.getSemesters().add(s);


        m = memberRepository.save(m);

        s.getMembers().add(m);

        semesterRepository.save(s);

        return new ResponseEntity<Member>(m, HttpStatus.OK);
    }

}
