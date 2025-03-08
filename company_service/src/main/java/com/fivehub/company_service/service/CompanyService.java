package com.fivehub.company_service.service;

import com.fivehub.company_service.model.CompanyDto;
import org.springframework.data.domain.Page;


public interface CompanyService {
    CompanyDto getCompanyById(int id);

    CompanyDto getCompanyByName(String name);

    Page<CompanyDto> getAllCompanies(int page, int size);
}