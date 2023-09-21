package com.cst438.domain;

public record AssignmentDTO(int id, String assignmentName, String dueDate, String courseTitle, int courseId) {
	
	public AssignmentDTO(String assignmentName, String dueDate, String courseTitle, int courseId) {
        this(0, assignmentName, dueDate, courseTitle, courseId);
	}
	
	public int getId() {
		return id;
	}

	public String assignmentName() {
		return assignmentName;
	}

	public String dueDate() {
		return dueDate;
	}

	public String courseTitle() {
		return courseTitle;
	}

	public int courseId() {
		return courseId;
	}




	


	
	
	
}

