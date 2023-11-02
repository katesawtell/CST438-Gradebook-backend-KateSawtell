package com.cst438.services;

import com.cst438.DTO.FinalGradeDTO;

public interface RegistrationService {
	
	public void sendFinalGrades(int course_id , FinalGradeDTO[] grades);

}
