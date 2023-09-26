package com.cst438.domain;

public record AssignmentDTO(int id, String assignmentName, String dueDate, String courseTitle, int courseId) {
	
	public AssignmentDTO(String assignmentName, String dueDate, String courseTitle, int courseId) {
        this(0, assignmentName, dueDate, courseTitle, courseId);
	}
	
}

