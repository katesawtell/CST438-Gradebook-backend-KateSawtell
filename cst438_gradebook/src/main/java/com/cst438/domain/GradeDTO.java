package com.cst438.domain;

public record GradeDTO (int assignmentGradeId, String name, String email, Integer grade) {

	public int assignmentGradeId() {
		return assignmentGradeId;
	}

	public String name() {
		return name;
	}

	public String email() {
		return email;
	}

	public Integer grade() {
		return grade;
	}

}
