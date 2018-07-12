package edu.ua.cs.acm.services;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.domain.Semester;
import java.time.LocalDateTime;

/**
 * Created by jzarobsky on 11/21/17.
 */
public interface SemesterService {
    Semester saveSemester(Semester semester) throws Exception;
    Semester getCurrentSemester();
    int currentSemesterId();
    LocalDateTime currentDueDate();
    Integer memberIsPaid(Member m);
//    void deleteSemester(Semester semester);
}
