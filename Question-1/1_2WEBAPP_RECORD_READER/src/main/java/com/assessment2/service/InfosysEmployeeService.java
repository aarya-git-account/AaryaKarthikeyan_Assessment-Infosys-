package com.assessment2.service;

import com.assessment2.dto.*;
import com.assessment2.repository.InfosysEmployeeRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class InfosysEmployeeService {

    private final InfosysEmployeeRepository employeeRepository;
    private final Cache<Long, EmployeeCachedDTO> employeeCache;

    public InfosysEmployeeService(InfosysEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    public List<EmployeeSalaryDTO> processSalariesSafely(int limit) {
        return employeeRepository.findWithLimit(limit).stream()
                .map(emp -> {
                    double tax = emp.getSalary() * 0.2;
                    return new EmployeeSalaryDTO(
                        emp.getEmployeeId(),
                        emp.getFirstName() + " " + emp.getLastName(),
                        emp.getSalary(),
                        tax,
                        emp.getSalary() - tax
                    );
                })
                .collect(Collectors.toList());
    }
    
    public Page<EmployeeSalaryDTO> getPaginatedSalaries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findAll(pageable)
                .map(emp -> {
                    double tax = emp.getSalary() * 0.2;
                    return new EmployeeSalaryDTO(
                            emp.getEmployeeId(),
                            emp.getFirstName() + " " + emp.getLastName(),
                            emp.getSalary(),
                            tax,
                            emp.getSalary() - tax
                    );
                });
    }

   
    @Async
    public CompletableFuture<EmployeeAsyncDTO> processEmployeeAsync(Long employeeId) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return employeeRepository.findById(employeeId)
                .map(emp -> CompletableFuture.completedFuture(
                        new EmployeeAsyncDTO(
                                emp.getEmployeeId(),
                                emp.getEmpEmail(),
                                emp.getPhoneNumber(),
                                "Processed successfully"
                        )))
                .orElse(CompletableFuture.completedFuture(
                        new EmployeeAsyncDTO(null, null, null, "Employee not found")));
    }

 
    public EmployeeCachedDTO getEmployeeWithCache(Long employeeId) {
        return employeeCache.get(employeeId, key -> 
            employeeRepository.findById(employeeId)
                    .map(emp -> new EmployeeCachedDTO(
                            emp.getEmployeeId(),
                            emp.getFirstName(),
                            emp.getLastName(),
                            emp.getJobTitle(),
                            emp.getSalary(),
                            false // Not from cache
                    ))
                    .orElse(null)
        );
    }
}