package com.cst438.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
@CrossOrigin 
public class AssignmentController {
	
	@Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    CourseRepository courseRepository;
    
    
//get all assignments for the instructor
    @GetMapping("/assignment")
    public ResponseEntity<AssignmentDTO[]> getAllAssignmentsForInstructor() {
        String instructorEmail = "dwisneski@csumb.edu";
        List<Assignment> assignments = assignmentRepository.findByEmail(instructorEmail);
        AssignmentDTO[] result = new AssignmentDTO[assignments.size()];
        for (int i = 0; i < assignments.size(); i++) {
            Assignment as = assignments.get(i);
            AssignmentDTO dto = new AssignmentDTO(
                    as.getId(),
                    as.getName(),
                    as.getDueDate().toString(),
                    as.getCourse().getTitle(),
                    as.getCourse().getCourse_id());
            result[i] = dto;
        }
        return ResponseEntity.ok(result);
    }
    
  
//find assignment with id
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<AssignmentDTO> getAssignment(@PathVariable("assignmentId") int assignmentId) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(assignmentId);
        if (optionalAssignment.isPresent()) {
            Assignment assignment = optionalAssignment.get();
            AssignmentDTO dto = new AssignmentDTO(
                    assignment.getId(),
                    assignment.getName(),
                    assignment.getDueDate().toString(),
                    assignment.getCourse().getTitle(),
                    assignment.getCourse().getCourse_id());
            return ResponseEntity.ok(dto);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found");
        }
    }
//create assignment
    @PostMapping("/assignment")
    public ResponseEntity<Integer> createAssignment(@RequestBody AssignmentDTO assignmentDTO) {
        Optional<Course> optionalCourse = courseRepository.findById(assignmentDTO.courseId());
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            Assignment newAssignment = new Assignment();
            newAssignment.setName(assignmentDTO.assignmentName());
            newAssignment.setDueDate(Date.valueOf(assignmentDTO.dueDate()));
            newAssignment.setCourse(course);
            Assignment savedAssignment = assignmentRepository.save(newAssignment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAssignment.getId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
    }
 
//update assignment
    @PutMapping("/assignment/{assignmentId}")
    public ResponseEntity<Integer> updateAssignment(@PathVariable("assignmentId") int assignmentId, @RequestBody AssignmentDTO assignmentDTO) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(assignmentId);
        if (optionalAssignment.isPresent()) {
            Assignment existingAssignment = optionalAssignment.get();
            existingAssignment.setName(assignmentDTO.assignmentName());
            existingAssignment.setDueDate(Date.valueOf(assignmentDTO.dueDate()));
            assignmentRepository.save(existingAssignment);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found");
        }
    }
    
//delete assignment
    
    @DeleteMapping("/assignment/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(@PathVariable("assignmentId") int assignmentId) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(assignmentId);

        if (optionalAssignment.isPresent()) {
            Assignment assignment = optionalAssignment.get();

            // Check if the assignment has associated grades
            List<AssignmentGrade> grades = AssignmentGradeRepository.findByAssignment(assignment);

            if (!grades.isEmpty()) {
                // Assignment has grades, return a warning message
                String warningMessage = "Warning: This assignment has associated grades. Deleting it may impact students' records.";
                return ResponseEntity.status(HttpStatus.OK).body(warningMessage);
            }

            // No grades associated with the assignment, proceed with "soft" deletion
            assignmentRepository.deleteById(assignmentId);
            // Update the assignment in the repository
            assignmentRepository.save(assignment);

            return ResponseEntity.noContent().build();
        } else {
            // Assignment not found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found");
        }
    }



}
