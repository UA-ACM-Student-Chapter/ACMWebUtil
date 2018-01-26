package edu.ua.cs.acm.repositories;

import edu.ua.cs.acm.domain.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by jzarobsky on 11/21/17.
 */
public interface MemberRepository extends CrudRepository <Member, Integer> {
    Member findByCrimsonEmail(String emailAddress);
    @Query(value = "SELECT * FROM Member WHERE member_id NOT IN (SELECT member_id FROM MemberSemesterLink WHERE semester_id = ?1)", nativeQuery = true)
    List<Member> unpaidForSemester(int semesterId);
    @Query(value = "SELECT * FROM Member", nativeQuery = true)
    List <Member> allMembers();
}
