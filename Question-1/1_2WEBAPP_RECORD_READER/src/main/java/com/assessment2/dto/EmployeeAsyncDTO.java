package com.assessment2.dto;

public record EmployeeAsyncDTO(
	    Long employeeId,
	    String email,
	    String phoneNumber,
	    String processingStatus
	) {}
