package edu.ua.cs.acm.services.impl;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.repositories.MemberRepository;
import edu.ua.cs.acm.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jzarobsky on 11/21/17.
 */
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
}
