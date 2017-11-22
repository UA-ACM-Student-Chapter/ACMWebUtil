package edu.ua.cs.acm.repositories;

import edu.ua.cs.acm.domain.Semester;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

/**
 * Created by jzarobsky on 11/21/17.
 */
public interface SemesterRepository extends CrudRepository <Semester, Integer> {
    // Semester findByStartDateBeforeAndEndDateAfter(Date date);
}
