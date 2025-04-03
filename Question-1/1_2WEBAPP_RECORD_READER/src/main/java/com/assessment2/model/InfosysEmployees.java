package com.assessment2.model;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "INFOSYS_EMPLOYEES")
public class InfosysEmployees {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String firstName;
    private String lastName;
    private String empEmail;
    private String phoneNumber;
    private String jobTitle;
    private Date hireDate;
    private Double salary;
    private Integer departmentId;

    public InfosysEmployees() {
    }

    public InfosysEmployees(Long employeeId, String firstName, String lastName, 
                          String empEmail, String phoneNumber, String jobTitle, 
                          Date hireDate, Double salary, Integer departmentId) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.empEmail = empEmail;
        this.phoneNumber = phoneNumber;
        this.jobTitle = jobTitle;
        this.hireDate = hireDate;
        this.salary = salary;
        this.departmentId = departmentId;
    }

    // Getters and Setters for all fields
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "InfosysEmployees{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", empEmail='" + empEmail + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", hireDate=" + hireDate +
                ", salary=" + salary +
                ", departmentId=" + departmentId +
                '}';
    }
}