package com.cst438.DTO;

public record GradeDTO (int assignmentGradeId, String name, String email, Integer grade) {

	public GradeDTO (String name, String email, Integer grade) {
		this(0, name, email, grade);
	}
}
