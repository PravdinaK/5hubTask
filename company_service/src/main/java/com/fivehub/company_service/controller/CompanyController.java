package com.fivehub.company_service.controller;

import com.fivehub.company_service.model.CompanyDto;
import com.fivehub.company_service.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@AllArgsConstructor
@Tag(name = "Компании", description = "Операции с компаниями")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "Получить компанию по ID вместе с сотрудниками")
    @GetMapping("/id/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable("id") int id) {
        CompanyDto companyDto = companyService.getCompanyById(id);
        return ResponseEntity.ok(companyDto);
    }

    @Operation(summary = "Получить компанию по наименованию вместе с сотрудниками")
    @GetMapping("/name/{name}")
    public ResponseEntity<CompanyDto> getCompanyByName(@PathVariable("name") String name) {
        CompanyDto companyDto = companyService.getCompanyByName(name);
        return ResponseEntity.ok(companyDto);
    }

    @Operation(summary = "Получить список всех компаний с их сотрудниками")
    @GetMapping("/all")
    public ResponseEntity<Page<CompanyDto>> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CompanyDto> companies = companyService.getAllCompanies(page, size);
        return ResponseEntity.ok(companies);
    }
}