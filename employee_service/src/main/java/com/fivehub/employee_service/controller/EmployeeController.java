package com.fivehub.employee_service.controller;

import com.fivehub.employee_service.model.EmployeeDto;
import com.fivehub.employee_service.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
@Tag(name = "Работники", description = "Операции с работниками")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/id/{id}")
    @Operation(summary = "Получить работника по ID")
    public EmployeeDto getUserById(@PathVariable int id) {
        return employeeService.getUserById(id);
    }

    @GetMapping("/name/{last_name}")
    @Operation(summary = "Получить работника по фамилии")
    public EmployeeDto getUserByLastName(@PathVariable(name = "last_name") String lastName) {
        return employeeService.getUserByLastName(lastName);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить всех работников")
    public Collection<EmployeeDto> getAllUsers() {
        return employeeService.getAllUsers();
    }

    @GetMapping("/employee/{company_id}")
    @Operation(summary = "Получить всех работников компании по ID компании")
    public Collection<EmployeeDto> getEmployeesByCompanyId(@PathVariable(name = "company_id") int companyId) {
        return employeeService.getEmployeesByCompanyId(companyId);
    }
}