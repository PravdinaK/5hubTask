package com.fivehub.employee_service.model;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class EmployeeMapper {

    private final ModelMapper modelMapper;

    public EmployeeDto toDto(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }

    public Employee toEntity(EmployeeDto dto) {
        return modelMapper.map(dto, Employee.class);
    }
}