package com.assessment2.dto;

public record EmployeeSalaryDTO(
	    Long employeeId,
	    String fullName,
	    Double salary,
	    Double taxDeduction,
	    Double netSalary
	) {}