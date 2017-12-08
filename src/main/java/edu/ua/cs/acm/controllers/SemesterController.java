package edu.ua.cs.acm.controllers;

import com.sun.org.apache.regexp.internal.RE;
import edu.ua.cs.acm.domain.Semester;
import edu.ua.cs.acm.services.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jzarobsky on 11/21/17.
 */
@RestController
@RequestMapping("/semester")
public class SemesterController {

    private final SemesterService semesterService;
    private final String authKey;

    @Autowired
    public SemesterController(SemesterService semesterService, @Value("${authorization-key}") String authKey) {
        this.semesterService = semesterService;
        this.authKey = authKey;
    }

    @GetMapping()
    public ResponseEntity<Semester> getSemester() {
        return ResponseEntity.ok(semesterService.getCurrentSemester());
    }

    @PostMapping()
    public ResponseEntity<Semester> createSemester(@RequestBody Semester semester,
                                                   @RequestHeader(name="Authorization", required = true) String authorization) {

        if(!authKey.equals(authorization)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            semesterService.saveSemester(semester);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(semester);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Semester> updateSemester(@RequestBody Semester semester,
                                                   @PathVariable("id") int id,
                                                   @RequestHeader(name="Authorization", required=true) String authorization
    ) {
        if(!authKey.equals(authorization)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        semester.setId(id);
        try {
            semesterService.saveSemester(semester);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(semester);
    }

}
