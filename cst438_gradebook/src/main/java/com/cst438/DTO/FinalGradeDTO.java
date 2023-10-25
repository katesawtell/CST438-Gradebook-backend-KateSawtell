package com.cst438.DTO;

public record FinalGradeDTO (String studentEmail, String studentName, String grade, int courseId) {

	public String studentEmail() {
		return studentEmail;
	}

	public String studentName() {
		return studentName;
	}

	public String grade() {
		return grade;
	}

	public int courseId() {
		return courseId;
	}

	
}