package edu.ua.cs.acm.services.impl;

import edu.ua.cs.acm.domain.Member;
import edu.ua.cs.acm.domain.Semester;
import edu.ua.cs.acm.repositories.SemesterRepository;
import edu.ua.cs.acm.services.SemesterService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created by jzarobsky on 11/21/17.
 */
@Service
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;

    public SemesterServiceImpl(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    @Override
    public Semester saveSemester(Semester semester) throws Exception {
        Semester semester1 =
                semesterRepository.findByStartDateBeforeAndEndDateAfter(semester.getStartDate().toLocalDate().toString(), semester.getStartDate().toLocalDate().toString());
        Semester semester2 =
                semesterRepository.findByStartDateBeforeAndEndDateAfter(semester.getEndDate().toLocalDate().toString(), semester.getEndDate().toLocalDate().toString());

        if(semester1 != null || semester2 != null) {
            throw new Exception("Semesters cannot have overlapping dates");
        }

        return this.semesterRepository.save(semester);
    }

    @Override
    public Semester getCurrentSemester() {
        return this.semesterRepository.currentSemester();
    }

    @Override
    public LocalDateTime currentDueDate() {
        return this.semesterRepository.currentDueDate();
    }

    @Override
    public Integer currentSemesterId() {
        Semester currentSemester = this.semesterRepository.currentSemester();
        if (currentSemester != null) {
            return currentSemester.getId();
        }
        return null;
    }

    @Override
    public Integer memberIsPaid(Member m) {
        Integer memberID = m.getId();
        Integer semesterID = semesterRepository.currentSemester().getId();

        return semesterRepository.isPaid(memberID, semesterID);
    }
}
