
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.controllers.AssignmentController;
import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JunitTestAssignment {

    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private AssignmentController assignmentController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllAssignmentsForInstructor() {
        // Mock data
        String instructorEmail = "dwisneski@csumb.edu";
        List<Assignment> mockAssignments = new ArrayList<>();
        mockAssignments.add(new Assignment(1, "Assignment 1", Date.valueOf("2023-09-20"), new Course("Course 1", "CS101")));
        mockAssignments.add(new Assignment(2, "Assignment 2", Date.valueOf("2023-09-21"), new Course("Course 2", "CS102")));

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
        assertEquals("Assignment 1", assignmentDTOs[0].getAssignmentName());
        assertEquals("2023-09-20", assignmentDTOs[0].getDueDate());
        assertEquals("Course 1", assignmentDTOs[0].getCourseTitle());
        assertEquals("CS101", assignmentDTOs[0].getCourseId());
        // Check other assertions for the second assignment DTO
    }
    @Test
    public void testGetAssignment() {
        int assignmentId = 1;
        Assignment mockAssignment = new Assignment(assignmentId, "Assignment 1", Date.valueOf("2023-09-20"), new Course("Course 1", "CS101"));

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(mockAssignment));

        ResponseEntity<AssignmentDTO> responseEntity = assignmentController.getAssignment(assignmentId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        AssignmentDTO assignmentDTO = responseEntity.getBody();
        assertEquals(assignmentId, assignmentDTO.getId());
        assertEquals("Assignment 1", assignmentDTO.getAssignmentName());
        assertEquals("2023-09-20", assignmentDTO.getDueDate());
        assertEquals("Course 1", assignmentDTO.getCourseTitle());
        assertEquals("CS101", assignmentDTO.getCourseId());
    }

    @Test(expected = ResponseStatusException.class)
    public void testGetAssignmentNotFound() {
        int assignmentId = 1;

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        assignmentController.getAssignment(assignmentId);
    }

    @Test
    public void testCreateAssignment() {
        AssignmentDTO requestDTO = new AssignmentDTO("New Assignment", "2023-09-22", "New Course", "CS102");
        Course mockCourse = new Course("New Course", "CS102");

        when(courseRepository.findById("CS102")).thenReturn(Optional.of(mockCourse));
        when(assignmentRepository.save(any())).thenReturn(new Assignment(1, requestDTO.getAssignmentName(), Date.valueOf(requestDTO.getDueDate()), mockCourse));

        ResponseEntity<Integer> responseEntity = assignmentController.createAssignment(requestDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().intValue());
    }

    @Test(expected = ResponseStatusException.class)
    public void testCreateAssignmentCourseNotFound() {
        AssignmentDTO requestDTO = new AssignmentDTO("New Assignment", "2023-09-22", "Nonexistent Course", "CS103");

        when(courseRepository.findById("CS103")).thenReturn(Optional.empty());

        assignmentController.createAssignment(requestDTO);
    }

    @Test
    public void testUpdateAssignment() {
        int assignmentId = 1;
        AssignmentDTO requestDTO = new AssignmentDTO("Updated Assignment", "2023-09-23", "Updated Course", "CS103");
        Assignment mockAssignment = new Assignment(assignmentId, "Original Assignment", Date.valueOf("2023-09-20"), new Course("Original Course", "CS101"));

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(mockAssignment));
        when(assignmentRepository.save(any())).thenReturn(new Assignment(assignmentId, requestDTO.getAssignmentName(), Date.valueOf(requestDTO.getDueDate()), new Course(requestDTO.getCourseTitle(), requestDTO.getCourseId())));

        ResponseEntity<Integer> responseEntity = assignmentController.updateAssignment(assignmentId, requestDTO);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test(expected = ResponseStatusException.class)
    public void testUpdateAssignmentNotFound() {
        int assignmentId = 1;
        AssignmentDTO requestDTO = new AssignmentDTO("Updated Assignment", "2023-09-23", "Updated Course", "CS103");

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        assignmentController.updateAssignment(assignmentId, requestDTO);
    }

    @Test
    public void testDeleteAssignment() {
        int assignmentId = 1;
        Assignment mockAssignment = new Assignment(assignmentId, "Assignment 1", Date.valueOf("2023-09-20"), new Course("Course 1", "CS101"));
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
        Assignment mockAssignment = new Assignment(assignmentId, "Assignment 1", Date.valueOf("2023-09-20"), new Course("Course 1", "CS101"));
        List<AssignmentGrade> mockGrades = new ArrayList<>();
        mockGrades.add(new AssignmentGrade(mockAssignment, new Enrollment(), 90));

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(mockAssignment));
        when(AssignmentGradeRepository.findByAssignment(mockAssignment)).thenReturn(mockGrades);

        ResponseEntity<String> responseEntity = assignmentController.deleteAssignment(assignmentId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }




}
