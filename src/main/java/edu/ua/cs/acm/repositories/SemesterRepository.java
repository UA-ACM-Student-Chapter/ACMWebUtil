package edu.ua.cs.acm.repositories;

import edu.ua.cs.acm.domain.Semester;
import org.apache.tomcat.jni.Local;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by jzarobsky on 11/21/17.
 */
public interface SemesterRepository extends CrudRepository <Semester, Integer> {
    Semester findByStartDateBeforeAndEndDateAfter(LocalDateTime startDate, LocalDateTime endTime);
}
