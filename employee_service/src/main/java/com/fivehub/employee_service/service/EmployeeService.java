package com.fivehub.employee_service.service;

import com.fivehub.employee_service.model.EmployeeDto;

import java.util.Collection;

public interface EmployeeService {
    Collection<EmployeeDto> getAllUsers();

    EmployeeDto getUserById(int id);

    EmployeeDto getUserByLastName(String lastName);

    Collection<EmployeeDto> getEmployeesByCompanyId(int companyId);
}