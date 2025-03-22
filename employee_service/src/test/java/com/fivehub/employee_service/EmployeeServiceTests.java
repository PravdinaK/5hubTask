package com.fivehub.employee_service;

import com.fivehub.employee_service.exception.EmployeeNotFoundException;
import com.fivehub.employee_service.model.Employee;
import com.fivehub.employee_service.model.EmployeeDto;
import com.fivehub.employee_service.model.EmployeeMapper;
import com.fivehub.employee_service.repository.EmployeeRepository;
import com.fivehub.employee_service.service.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private static final Employee employee1 = new Employee(1, "Иван", "Иванов", 1, "+79161234567");
    private static final EmployeeDto employeeDto1 = new EmployeeDto(1, "Иван", "Иванов", "+79161234567");

    @Test
    public void getUserById_Success() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee1));
        when(employeeMapper.toDto(employee1)).thenReturn(employeeDto1);

        EmployeeDto result = employeeService.getUserById(1);

        Assertions.assertEquals(employeeDto1, result);
        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void getUserById_NotFound() {
        when(employeeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getUserById(99));
        verify(employeeRepository, times(1)).findById(99);
    }

    @Test
    void getUserByLastName_Success() {
        when(employeeRepository.findByLastName("Иванов")).thenReturn(Optional.of(employee1));
        when(employeeMapper.toDto(employee1)).thenReturn(employeeDto1);

        EmployeeDto result = employeeService.getUserByLastName("Иванов");

        Assertions.assertEquals(employeeDto1, result);
        verify(employeeRepository, times(1)).findByLastName("Иванов");
    }

    @Test
    void getUserByLastName_NotFound() {
        when(employeeRepository.findByLastName("Неизвестный")).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getUserByLastName("Неизвестный"));
        verify(employeeRepository, times(1)).findByLastName("Неизвестный");
    }

    @Test
    void getAllUsers_Success() {
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employee1));
        when(employeeMapper.toDto(employee1)).thenReturn(employeeDto1);

        Collection<EmployeeDto> result = employeeService.getAllUsers();

        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(employeeDto1));
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeesByCompanyId_Success() {
        when(employeeRepository.findByCompanyId(1)).thenReturn(Collections.singletonList(employee1));
        when(employeeMapper.toDto(employee1)).thenReturn(employeeDto1);

        Collection<EmployeeDto> result = employeeService.getEmployeesByCompanyId(1);

        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(employeeDto1));
        verify(employeeRepository, times(1)).findByCompanyId(1);
    }

    @Test
    void getEmployeesByCompanyId_EmptyList() {
        when(employeeRepository.findByCompanyId(99)).thenReturn(Collections.emptyList());

        Collection<EmployeeDto> result = employeeService.getEmployeesByCompanyId(99);

        Assertions.assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findByCompanyId(99);
    }
}