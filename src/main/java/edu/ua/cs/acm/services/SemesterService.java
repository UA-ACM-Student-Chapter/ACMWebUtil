package edu.ua.cs.acm.services;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.domain.Semester;

/**
 * Created by jzarobsky on 11/21/17.
 */
public interface SemesterService {
    Semester saveSemester(Semester semester) throws Exception;
    Semester getCurrentSemester();
    int currentSemesterId();
    int memberIsPaid(Member m);
    void deleteSemester(Semester semester);
}
