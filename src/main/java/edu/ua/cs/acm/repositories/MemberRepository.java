package edu.ua.cs.acm.repositories;

import edu.ua.cs.acm.domain.Member;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jzarobsky on 11/21/17.
 */
public interface MemberRepository extends CrudRepository <Member, Integer> {
    Member findByCrimsonEmail(String emailAddress);
}
