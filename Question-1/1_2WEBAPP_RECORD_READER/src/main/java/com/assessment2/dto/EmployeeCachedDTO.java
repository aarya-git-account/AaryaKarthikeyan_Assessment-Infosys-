package com.assessment2.dto;

public record EmployeeCachedDTO(
	    Long employeeId,
	    String firstName,
	    String lastName,
	    String jobTitle,
	    Double salary,
	    boolean fromCache
	) {}
