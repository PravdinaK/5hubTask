package com.fivehub.company_service;

import com.fivehub.company_service.config.FeignConfig;
import com.fivehub.company_service.model.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "employee-service", configuration = FeignConfig.class)
public interface EmployeeFeignClient {
    @GetMapping("/api/employees/employee/{company_id}")
    List<EmployeeDto> getEmployeesByCompanyId(@PathVariable("company_id") int companyId);
}