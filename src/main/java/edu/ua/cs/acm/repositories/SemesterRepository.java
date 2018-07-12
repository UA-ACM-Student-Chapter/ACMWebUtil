package edu.ua.cs.acm.repositories;

import edu.ua.cs.acm.domain.Semester;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by jzarobsky on 11/21/17.
 */
public interface SemesterRepository extends CrudRepository <Semester, Integer> {
    Semester findByStartDateBeforeAndEndDateAfter(LocalDateTime startDate, LocalDateTime endTime);

    @Query(value = "SELECT semester_id FROM Semester WHERE CURRENT_TIMESTAMP <= end_date AND CURRENT_TIMESTAMP > start_date", nativeQuery = true)
    int currentSemesterId();

    @Query(value = "SELECT due_date FROM Semester WHERE CURRENT_TIMESTAMP <= end_date AND CURRENT_TIMESTAMP > start_date", nativeQuery = true)
    LocalDateTime currentDueDate();

    @Query(value = "SELECT member_id FROM MemberSemesterLink WHERE member_id = ?1 AND semester_id = ?2", nativeQuery = true)
    Integer isPaid(Integer memberID, Integer semesterID);
}