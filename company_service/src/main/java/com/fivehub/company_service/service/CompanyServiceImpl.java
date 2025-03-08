package com.fivehub.company_service.service;

import com.fivehub.company_service.EmployeeFeignClient;
import com.fivehub.company_service.exception.CompanyNotFoundException;
import com.fivehub.company_service.model.Company;
import com.fivehub.company_service.model.CompanyDto;
import com.fivehub.company_service.model.CompanyMapper;
import com.fivehub.company_service.model.EmployeeDto;
import com.fivehub.company_service.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final EmployeeFeignClient employeeFeignClient;

    @Override
    public CompanyDto getCompanyById(int id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));
        return requestEmployeesForCompany(company);
    }

    @Override
    public CompanyDto getCompanyByName(String name) {
        Company company = companyRepository.findByName(name)
                .orElseThrow(() -> new CompanyNotFoundException(name));
        return requestEmployeesForCompany(company);
    }

    @Override
    public Page<CompanyDto> getAllCompanies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Company> companyPage = companyRepository.findAll(pageable);
        return companyPage.map(this::requestEmployeesForCompany);
    }

    private CompanyDto requestEmployeesForCompany(Company company) {
        List<EmployeeDto> employees = employeeFeignClient.getEmployeesByCompanyId(company.getId());
        CompanyDto companyDto = companyMapper.toDto(company);
        companyDto.setEmployeeIds(company.getEmployeeIds());
        companyDto.setEmployees(employees);
        return companyDto;
    }
}