package com.fivehub.company_service.model;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CompanyMapper {

    private final ModelMapper modelMapper;

    public CompanyDto toDto(Company company) {
        return modelMapper.map(company, CompanyDto.class);
    }

    public Company toEntity(CompanyDto companyDto) {
        return modelMapper.map(companyDto, Company.class);
    }
}