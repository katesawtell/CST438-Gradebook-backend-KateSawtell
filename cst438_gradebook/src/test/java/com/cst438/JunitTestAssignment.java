
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.controllers.AssignmentController;
import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JunitTestAssignment {

	@Autowired
	private MockMvc mvc;
	
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentController assignmentController;

    //test if this gets all assignments for the instructor
    @Test
    public void testGetAllAssignmentsForInstructor() {
        // Mock data
        String instructorEmail = "dwisneski@csumb.edu";
        List<Assignment> mockAssignments = new ArrayList<>();
        Course mockCourse = new Course ("course 1", "wisneski", 2023, "Fall");
;
        mockAssignments.add(new Assignment(mockCourse, null, "Assignment 1", Date.valueOf("2023-09-20")));
        mockAssignments.add(new Assignment(mockCourse, null, "Assignment 2", Date.valueOf("2023-09-20")));        


        // Mock behavior of assignmentRepository.findByEmail
        when(assignmentRepository.findByEmail(instructorEmail)).thenReturn(mockAssignments);

        // Call the controller method
        ResponseEntity<AssignmentDTO[]> responseEntity = assignmentController.getAllAssignmentsForInstructor();

        // Check the HTTP status code
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Check the response body
        AssignmentDTO[] assignmentDTOs = responseEntity.getBody();
        assertEquals(2, assignmentDTOs.length);
        assertEquals(1, assignmentDTOs[0].getId());
        assertEquals("Assignment 1", assignmentDTOs[0].assignmentName());
        assertEquals("2023-09-20", assignmentDTOs[0].dueDate());
        assertEquals("Course 1", assignmentDTOs[0].courseTitle());
        assertEquals("CS101", assignmentDTOs[0].courseId());
        // Check other assertions for the second assignment DTO
    }
    @Test
    public void testGetAssignment() {
        int assignmentId = 1;
        Course mockCourse = new Course ("course 1", "wisneski", 2023, "Fall");
        Assignment mockAssignment = new Assignment(mockCourse, null, "Assignment 2", Date.valueOf("2023-09-20"));

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(mockAssignment));

        ResponseEntity<AssignmentDTO> responseEntity = assignmentController.getAssignment(assignmentId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        AssignmentDTO assignmentDTO = responseEntity.getBody();
        assertEquals(assignmentId, assignmentDTO.getId());
        assertEquals("Assignment 1", assignmentDTO.assignmentName());
        assertEquals("2023-09-20", assignmentDTO.dueDate());
        assertEquals("Course 1", assignmentDTO.courseTitle());
        assertEquals("CS101", assignmentDTO.courseId());
    }

    @Test(expected = ResponseStatusException.class)
    public void testGetAssignmentNotFound() {
        int assignmentId = 1;

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        assignmentController.getAssignment(assignmentId);
    }

    @Test
    public void testCreateAssignment() {
		MockHttpServletResponse response;
        Course mockCourse = new Course ("course 2", "wisneski", 2023, "Fall");

        Assignment mockAssignment = new Assignment(mockCourse, null, "Assignment 2", Date.valueOf("2023-09-20"));

        when(courseRepository.findById(0)).thenReturn(Optional.of(mockCourse));
        when(assignmentRepository.save(any())).thenReturn(new Assignment(1, assignmentDTO.assignmentName(), Date.valueOf(requestDTO.dueDate(), mockCourse));

        ResponseEntity<Integer> responseEntity = assignmentController.createAssignment(requestDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().intValue());
    }

    @Test(expected = ResponseStatusException.class)
    public void testCreateAssignmentCourseNotFound() {
        AssignmentDTO requestDTO = new AssignmentDTO("New Assignment", "2023-09-22", "Nonexistent Course", 102);

        when(courseRepository.findById("CS103")).thenReturn(Optional.empty());

        assignmentController.createAssignment(requestDTO);
    }

    @Test
    public void testUpdateAssignment() {
        int assignmentId = 1;
        Course mockCourse = new Course ("course 3", "wisneski", 2023, "Fall");
        AssignmentDTO requestDTO = new AssignmentDTO("Updated Assignment", "2023-09-23", "Updated Course", 102);
        Assignment mockAssignment = new Assignment(mockCourse, null, "Assignment 1", Date.valueOf("2023-09-20")));

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(mockAssignment));
        when(assignmentRepository.save(any())).thenReturn(new Assignment(assignmentId, requestDTO.getAssignmentName(), Date.valueOf(requestDTO.getDueDate()), new Course(requestDTO.getCourseTitle(), requestDTO.getCourseId())));

        ResponseEntity<Integer> responseEntity = assignmentController.updateAssignment(assignmentId, requestDTO);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test(expected = ResponseStatusException.class)
    public void testUpdateAssignmentNotFound() {
        int assignmentId = 1;
        AssignmentDTO requestDTO = new AssignmentDTO("Updated Assignment", "2023-09-23", "Updated Course", 102);

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        assignmentController.updateAssignment(assignmentId, requestDTO);
    }

    @Test
    public void testDeleteAssignment() {
        int assignmentId = 1;
        Course mockCourse = new Course ("course 1", "wisneski", 2023, "Fall");
        Assignment mockAssignment = new Assignment(mockCourse, null, "Assignment 1", Date.valueOf("2023-09-20"));
        List<AssignmentGrade> mockGrades = new ArrayList<>();

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(mockAssignment));
        when(assignmentRepository.save(any())).thenReturn(mockAssignment);
        when(AssignmentGradeRepository.findByAssignment(mockAssignment)).thenReturn(mockGrades);

        ResponseEntity<String> responseEntity = assignmentController.deleteAssignment(assignmentId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteAssignmentWithGrades() {
        int assignmentId = 1;
        Course mockCourse = new Course ("course 1", "wisneski", 2023, "Fall");
        Assignment mockAssignment = new Assignment(mockCourse, null, "Assignment 1", Date.valueOf("2023-09-20"));
        List<AssignmentGrade> mockGrades = new ArrayList<>();

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(mockAssignment));
        when(AssignmentGradeRepository.findByAssignment(mockAssignment)).thenReturn(mockGrades);

        ResponseEntity<String> responseEntity = assignmentController.deleteAssignment(assignmentId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }




}
