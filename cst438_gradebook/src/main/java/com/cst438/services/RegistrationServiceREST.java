package com.cst438.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.FinalGradeDTO;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Enrollment;

@Service
@ConditionalOnProperty(prefix = "registration", name = "service", havingValue = "rest")
@RestController
public class RegistrationServiceREST implements RegistrationService {

	
	RestTemplate restTemplate = new RestTemplate();
	
	@Value("${registration.url}") 
	String registration_url;
	
	public RegistrationServiceREST() {
		System.out.println("REST registration service ");
	}
	
	@Override
	public void sendFinalGrades(int course_id , FinalGradeDTO[] grades) { 
		
		restTemplate.put(registration_url+ "/course/" + course_id + "/finalgrades", grades);
		
	}
	
	@Autowired
	CourseRepository courseRepository;

	@Autowired
	EnrollmentRepository enrollmentRepository;

	
	/*
	 * endpoint used by registration service to add an enrollment to an existing
	 * course.
	 */
	
	
	@PostMapping("/enrollment")
	@Transactional
	public EnrollmentDTO addEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
	    // Create a new Enrollment entity from the DTO.
		
		System.out.println("GradeBook addEnrollment "+enrollmentDTO);

	    Enrollment enrollment = new Enrollment();
	    enrollment.setStudentName(enrollmentDTO.studentName());
	    enrollment.setStudentEmail(enrollmentDTO.studentEmail());


	    Optional<Course> course = courseRepository.findById(enrollmentDTO.courseId());
	    
	    
	    // makes sure a course is there 
	    if (course.isPresent()) {
	        Course courseFRL = course.get(); 
	        enrollment.setCourse(courseFRL);
	    }
	   
	    return enrollmentDTO;
	}
	




}
