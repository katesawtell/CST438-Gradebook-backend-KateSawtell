package com.cst438.domain;

public record  EnrollmentDTO (int id, String studentEmail, String studentName, int courseId) {
	
	public EnrollmentDTO(String studentEmail, String studentName, int courseId) {
        this(0, studentEmail, studentName, courseId);
	}
	
}
