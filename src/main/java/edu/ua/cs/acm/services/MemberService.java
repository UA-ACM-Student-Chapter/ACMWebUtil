package edu.ua.cs.acm.services;

import edu.ua.cs.acm.domain.Member;

/**
 * Created by jzarobsky on 11/21/17.
 */
public interface MemberService {
    Member getByCrimsonEmail(String email);
    Member getById(int id);
    Member save(Member member);
    void delete(Member member);
}
