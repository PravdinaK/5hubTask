package com.fivehub.employee_service.service;

import com.fivehub.employee_service.exception.EmployeeNotFoundException;
import com.fivehub.employee_service.model.EmployeeMapper;
import com.fivehub.employee_service.model.Employee;
import com.fivehub.employee_service.model.EmployeeDto;
import com.fivehub.employee_service.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeDto getUserById(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeDto getUserByLastName(String lastName) {
        Employee employee = employeeRepository.findByLastName(lastName)
                .orElseThrow(() -> new EmployeeNotFoundException(lastName));
        return employeeMapper.toDto(employee);
    }

    @Override
    public Collection<EmployeeDto> getAllUsers() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<EmployeeDto> getEmployeesByCompanyId(int companyId) {
        return employeeRepository.findByCompanyId(companyId).stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }
}