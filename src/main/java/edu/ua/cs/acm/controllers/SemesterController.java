package edu.ua.cs.acm.controllers;

import edu.ua.cs.acm.domain.Semester;
import edu.ua.cs.acm.services.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jzarobsky on 11/21/17.
 */
@RestController
@RequestMapping("/semester")
public class SemesterController {

    private final SemesterService semesterService;

    @Autowired
    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @GetMapping()
    public ResponseEntity<Semester> getSemester() {
        return ResponseEntity.ok(semesterService.getCurrentSemester());
    }

    @PostMapping()
    public ResponseEntity<Semester> createSemester(@RequestBody Semester semester) {
        try {
            semesterService.saveSemester(semester);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(semester);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Semester> updateSemester(@RequestBody Semester semester, @PathVariable("id") int id) {
        semester.setId(id);
        try {
            semesterService.saveSemester(semester);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(semester);
    }

}
