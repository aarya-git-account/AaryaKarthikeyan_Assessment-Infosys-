package com.assessment2.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import com.assessment2.service.InfosysEmployeeService;
import com.assessment2.dto.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/employees")
public class InfosysEmployeeController {

    private final InfosysEmployeeService employeeService;

    public InfosysEmployeeController(InfosysEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //using limit to fetch only 100 rows by default
    @GetMapping("/process-salaries/safe")
    public List<EmployeeSalaryDTO> processSalariesSafely(
            @RequestParam(name = "limit", defaultValue = "100") int limit) {
        if (limit > 1000) {
            throw new IllegalArgumentException("Maximum limit is 1000 records");
        }
        return employeeService.processSalariesSafely(limit);
    }
    
    //Paginated salary processing 
    @GetMapping("/process-salaries/paginated")
    public Page<EmployeeSalaryDTO> getPaginatedSalaryInfo(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {
        return employeeService.getPaginatedSalaries(page, size);
    }

    // Async processing
    @GetMapping("/async/{id}")
    public CompletableFuture<EmployeeAsyncDTO> processEmployeeAsync(@PathVariable("id") Long id) {
        return employeeService.processEmployeeAsync(id);
    }

    // Cached employee data
    @GetMapping("/cached/{id}")
    public EmployeeCachedDTO getEmployeeWithCache(@PathVariable("id") Long employeeId) {
        return employeeService.getEmployeeWithCache(employeeId);
    }
}