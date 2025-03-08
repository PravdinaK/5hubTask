package com.fivehub.employee_service.initializer;

import com.fivehub.employee_service.model.Employee;
import com.fivehub.employee_service.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDatabaseInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    public EmployeeDatabaseInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (employeeRepository.count() == 0) {
            Employee employee1 = new Employee("Иван", "Иванов", 1, "1234567890");
            Employee employee2 = new Employee("Алексей", "Алексеев", 2, "2345678901");
            Employee employee3 = new Employee("Петр", "Петров", 1, "3456789012");

            employeeRepository.save(employee1);
            employeeRepository.save(employee2);
            employeeRepository.save(employee3);
            System.out.println("Данные о сотрудниках были успешно добавлены.");
        }
    }
}