package com.fivehub.employee_service.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(int id) {
        super(String.format("Employee with id %d not found", id));
    }

    public EmployeeNotFoundException(String lastName) {
        super(String.format("Employee with last name %s not found", lastName));
    }
}