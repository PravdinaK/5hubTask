package com.fivehub.employee_service.repository;

import com.fivehub.employee_service.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.Optional;

@RequestMapping
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByLastName(String lastName);

    Collection<Employee> findByCompanyId(int companyId);
}