package com.fivehub.company_service;

import com.fivehub.company_service.controller.CompanyController;
import com.fivehub.company_service.exception.CompanyNotFoundException;
import com.fivehub.company_service.exception.FeignClientException;
import com.fivehub.company_service.model.CompanyDto;
import com.fivehub.company_service.model.EmployeeDto;
import com.fivehub.company_service.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CompanyController.class)
class CompanyControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CompanyService companyService;

    @Mock
    private EmployeeFeignClient employeeFeignClient;

    @Test
    void test_getCompanyById_success() throws Exception {
        CompanyDto companyDto = CompanyDto.builder()
                .id(1)
                .name("TechCorp")
                .budget(1000000L)
                .employeeIds(List.of(1, 2))
                .build();

        when(companyService.getCompanyById(1)).thenReturn(companyDto);

        mockMvc.perform(get("/api/company/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TechCorp"))
                .andExpect(jsonPath("$.budget").value(1000000));
    }

    @Test
    void test_getCompanyById_whenCompanyNotFound() throws Exception {
        doThrow(CompanyNotFoundException.class).when(companyService).getCompanyById(anyInt());

        mockMvc.perform(get("/api/company/id/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getCompanyByName_success() throws Exception {
        CompanyDto companyDto = CompanyDto.builder()
                .id(1)
                .name("TechCorp")
                .budget(1000000L)
                .employeeIds(List.of(1, 2))
                .build();

        when(companyService.getCompanyByName("TechCorp")).thenReturn(companyDto);

        mockMvc.perform(get("/api/company/name/TechCorp"))
                .andExpect(status().isOk())  // Ожидаем статус 200
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TechCorp"))
                .andExpect(jsonPath("$.budget").value(1000000));
    }

    @Test
    void test_getCompanyByName_whenCompanyNotFound() throws Exception {
        doThrow(CompanyNotFoundException.class).when(companyService).getCompanyByName(anyString());

        mockMvc.perform(get("/api/company/name/Unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getAllCompanies_success() throws Exception {
        List<CompanyDto> companyDtos = List.of(
                CompanyDto.builder().id(1).name("TechCorp").budget(1000000L).employeeIds(List.of(1, 2)).build(),
                CompanyDto.builder().id(2).name("SoftSolutions").budget(500000L).employeeIds(List.of(3)).build()
        );

        when(companyService.getAllCompanies(0, 10)).thenReturn(new PageImpl<>(companyDtos));

        mockMvc.perform(get("/api/company/all?page=0&size=10"))
                .andExpect(status().isOk())  // Ожидаем статус 200
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].name").value("SoftSolutions"));
    }

    @Test
    void test_getAllCompanies_emptyResult() throws Exception {
        when(companyService.getAllCompanies(0, 10)).thenReturn(Page.empty());

        mockMvc.perform(get("/api/company/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void getCompanyById_DataAccessException() throws Exception {
        when(companyService.getCompanyById(1)).thenThrow(new DataAccessException("Database access error") {});

        mockMvc.perform(get("/api/company/id/1"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$").value("Database access error"));
    }

    @Test
    void getCompanyById_FeignClientException() throws Exception {
        when(companyService.getCompanyById(1)).thenThrow(new FeignClientException("Feign client error"));

        mockMvc.perform(get("/api/company/id/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("Feign client error: Feign client error"));
    }
}