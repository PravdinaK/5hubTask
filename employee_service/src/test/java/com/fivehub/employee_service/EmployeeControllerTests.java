package com.fivehub.employee_service;

import com.fivehub.employee_service.controller.EmployeeController;
import com.fivehub.employee_service.exception.EmployeeNotFoundException;
import com.fivehub.employee_service.model.EmployeeDto;
import com.fivehub.employee_service.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    private static final EmployeeDto EMPLOYEE_1 = new EmployeeDto(1, "Иван", "Иванов", "+79161234567");
    private static final EmployeeDto EMPLOYEE_2 = new EmployeeDto(2, "Петр", "Петров", "+79161234568");

    @Test
    void getUserById_Success() throws Exception {
        when(employeeService.getUserById(1)).thenReturn(EMPLOYEE_1);

        mockMvc.perform(get("/api/employees/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EMPLOYEE_1.getId()))
                .andExpect(jsonPath("$.firstName").value(EMPLOYEE_1.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(EMPLOYEE_1.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(EMPLOYEE_1.getPhoneNumber()));
    }

    @Test
    void getUserById_NotFound() throws Exception {
        when(employeeService.getUserById(99)).thenThrow(new EmployeeNotFoundException(99));

        mockMvc.perform(get("/api/employees/id/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Employee with id 99 not found"));
    }

    @Test
    void getUserByLastName_Success() throws Exception {
        when(employeeService.getUserByLastName("Петров")).thenReturn(EMPLOYEE_2);

        mockMvc.perform(get("/api/employees/name/Петров"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EMPLOYEE_2.getId()))
                .andExpect(jsonPath("$.firstName").value(EMPLOYEE_2.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(EMPLOYEE_2.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(EMPLOYEE_2.getPhoneNumber()));
    }

    @Test
    void getUserByLastName_NotFound() throws Exception {
        when(employeeService.getUserByLastName("Неизвестный")).thenThrow(new EmployeeNotFoundException("Неизвестный"));

        mockMvc.perform(get("/api/employees/name/Неизвестный"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Employee with last name Неизвестный not found"));
    }

    @Test
    void getAllUsers_Success() throws Exception {
        List<EmployeeDto> employees = List.of(EMPLOYEE_1, EMPLOYEE_2);

        when(employeeService.getAllUsers()).thenReturn(employees);

        mockMvc.perform(get("/api/employees/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getEmployeesByCompanyId_Success() throws Exception {
        List<EmployeeDto> employees = List.of(EMPLOYEE_1);

        when(employeeService.getEmployeesByCompanyId(1)).thenReturn(employees);

        mockMvc.perform(get("/api/employees/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void getEmployeesByCompanyId_BadRequest() throws Exception {
        mockMvc.perform(get("/api/employees/employee/abc"))
                .andExpect(status().isBadRequest());
    }
}