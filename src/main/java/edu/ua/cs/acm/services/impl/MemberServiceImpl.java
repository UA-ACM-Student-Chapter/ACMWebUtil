package edu.ua.cs.acm.services.impl;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.domain.MemberSemesterLink;
import edu.ua.cs.acm.domain.Semester;
import edu.ua.cs.acm.repositories.MemberRepository;
import edu.ua.cs.acm.repositories.MemberSemesterLinkRepository;
import edu.ua.cs.acm.repositories.SemesterRepository;
import edu.ua.cs.acm.services.MemberService;
import edu.ua.cs.acm.services.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * Created by jzarobsky on 11/21/17.
 */
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberSemesterLinkRepository mslRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, MemberSemesterLinkRepository mslRepository) {
        this.memberRepository = memberRepository;
        this.mslRepository = mslRepository;
    }

    @Override
    public Member getByCrimsonEmail(String email) {
        return memberRepository.findByCrimsonEmail(email);
    }

    @Override
    public Member getById(int id) {
        return memberRepository.findOne(id);
    }

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public void delete(Member member) {
        memberRepository.delete(member);
    }

    @Override
    public List<Member> unpaidMembers(Semester semester) {
        return memberRepository.unpaidForSemester(semester.getId());
    }

    @Override
    public List<Member> allMembers() {
        return memberRepository.allMembers();
    }

    @Override
    public Integer updateShirtSize(Member memberToUpdate, String newShirtSize) {
        int memberId = memberToUpdate.getId();
        return memberRepository.updateShirtSize(memberId, newShirtSize);
    }

    @Override
    public Integer payForSemester(Member payingMember, int semesterId, String purchaseID) {
        int memberId = payingMember.getId();

        //Check if purchaseID is valid
        Boolean valid = true;

        if (valid) {
            MemberSemesterLink msl = new MemberSemesterLink(memberId, semesterId);
            mslRepository.save(msl);
        } else {
            return -1;
        }

        return 0;
    }

    // Scheduled for every Sunday @ 5pm
    @Scheduled(cron = "0 17 * * 0")
    public void sendOutInvoicesReminders() {
        // Note: this may or may not work with heroku depending on if the dyno is asleep.

    }

}
