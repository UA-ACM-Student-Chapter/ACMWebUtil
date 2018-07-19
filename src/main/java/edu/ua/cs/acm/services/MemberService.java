package edu.ua.cs.acm.services;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.domain.Semester;

import java.util.List;

/**
 * Created by jzarobsky on 11/21/17.
 */
public interface MemberService {
    Member getByCrimsonEmail(String email);
    Member getById(int id);
    Member save(Member member);
    void delete(Member member);

    List<Member> unpaidMembers(Semester semester);
    List<Member> paidMembers(Semester semester);
    List<Member> allMembers();

    Integer updateShirtSize(Member memberToUpdate, String newShirtSize);
    Integer payForSemester(Member payingMember, int semesterId, String purchaseID);
}
