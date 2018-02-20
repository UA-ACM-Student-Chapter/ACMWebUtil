package edu.ua.cs.acm.repositories;

import edu.ua.cs.acm.domain.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by jzarobsky on 11/21/17.
 */
public interface MemberRepository extends CrudRepository <Member, Integer> {
    @Query(value = "SELECT * FROM Member WHERE crimson_email = ?1", nativeQuery = true)
    Member findByCrimsonEmail(String emailAddress);

    @Query(value = "SELECT * FROM Member WHERE member_id NOT IN (SELECT member_id FROM MemberSemesterLink WHERE semester_id = ?1)", nativeQuery = true)
    List<Member> unpaidForSemester(int semesterId);
    @Query(value = "SELECT * FROM Member", nativeQuery = true)
    List <Member> allMembers();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Member SET shirt_size = ?2 WHERE member_id = ?1", nativeQuery = true)
    Integer updateShirtSize(int memberId, String newShirtSize);
}
