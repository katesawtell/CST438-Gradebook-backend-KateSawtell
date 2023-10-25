package com.cst438.DTO;

public record  EnrollmentDTO (int id, String studentEmail, String studentName, int courseId) {
	
	public EnrollmentDTO(String studentEmail, String studentName, int courseId) {
        this(0, studentEmail, studentName, courseId);
	}

	public int id() {
		return id;
	}
	

	public String studentEmail() {
		return studentEmail;
	}

	public String studentName() {
		return studentName;
	}

	public int courseId() {
		return courseId;
	}
	
	
}
