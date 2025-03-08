package com.fivehub.company_service;

import com.fivehub.company_service.exception.CompanyNotFoundException;
import com.fivehub.company_service.model.Company;
import com.fivehub.company_service.model.CompanyDto;
import com.fivehub.company_service.model.EmployeeDto;
import com.fivehub.company_service.repository.CompanyRepository;
import com.fivehub.company_service.service.CompanyServiceImpl;
import com.fivehub.company_service.model.CompanyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTests {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private EmployeeFeignClient employeeFeignClient;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company company1, company2;
    private CompanyDto companyDto1, companyDto2;
    private List<EmployeeDto> employeesForCompany1, employeesForCompany2;

    @BeforeEach
    void setUp() {
        company1 = createCompany(1, "TechCorp", 1_000_000L, List.of(1, 2));
        company2 = createCompany(2, "SoftSolutions", 500_000L, List.of(3));

        companyDto1 = createCompanyDto(1, "TechCorp", 1_000_000L, List.of(1, 2));
        companyDto2 = createCompanyDto(2, "SoftSolutions", 500_000L, List.of(3));

        employeesForCompany1 = List.of(
                new EmployeeDto(1, "Иван", "Иванов", "+79161234567"),
                new EmployeeDto(2, "Петр", "Петров", "+79161234568")
        );
        employeesForCompany2 = Collections.emptyList();
    }

    @ParameterizedTest
    @MethodSource("provideCompanies")
    void getCompanyById_Success(int companyId, String name, long budget, List<Integer> employeeIds, List<EmployeeDto> employees) {
        Company company = createCompany(companyId, name, budget, employeeIds);
        CompanyDto companyDto = createCompanyDto(companyId, name, budget, employeeIds);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);
        when(employeeFeignClient.getEmployeesByCompanyId(companyId)).thenReturn(employees);

        CompanyDto result = companyService.getCompanyById(companyId);

        assertAll(
                () -> assertEquals(companyDto.getId(), result.getId()),
                () -> assertEquals(companyDto.getName(), result.getName()),
                () -> assertEquals(companyDto.getBudget(), result.getBudget()),
                () -> assertEquals(companyDto.getEmployeeIds(), result.getEmployeeIds()),
                () -> assertEquals(employees, result.getEmployees())
        );

        verify(companyRepository).findById(companyId);
        verify(employeeFeignClient).getEmployeesByCompanyId(companyId);
    }

    @ParameterizedTest
    @ValueSource(ints = {99, 100})
    void getCompanyById_NotFound(int companyId) {
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> companyService.getCompanyById(companyId));

        verify(companyRepository).findById(companyId);
        verifyNoInteractions(employeeFeignClient);
    }


    @ParameterizedTest
    @MethodSource("provideCompanies")
    void getCompanyByName_Success(int companyId, String name, long budget, List<Integer> employeeIds, List<EmployeeDto> employees) {
        Company company = createCompany(companyId, name, budget, employeeIds);
        CompanyDto companyDto = createCompanyDto(companyId, name, budget, employeeIds);

        when(companyRepository.findByName(name)).thenReturn(Optional.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);
        when(employeeFeignClient.getEmployeesByCompanyId(companyId)).thenReturn(employees);

        CompanyDto result = companyService.getCompanyByName(name);

        assertAll(
                () -> assertEquals(companyDto.getId(), result.getId()),
                () -> assertEquals(companyDto.getName(), result.getName()),
                () -> assertEquals(companyDto.getBudget(), result.getBudget()),
                () -> assertEquals(companyDto.getEmployeeIds(), result.getEmployeeIds()),
                () -> assertEquals(employees, result.getEmployees())
        );

        verify(companyRepository).findByName(name);
        verify(employeeFeignClient).getEmployeesByCompanyId(companyId);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Unknown", "NonExistent"})
    void getCompanyByName_NotFound(String companyName) {
        when(companyRepository.findByName(companyName)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> companyService.getCompanyByName(companyName));

        verify(companyRepository).findByName(companyName);
        verifyNoInteractions(employeeFeignClient);
    }

    @Test
    void test_getAllCompanies_success() {
        List<Company> companyList = List.of(company1, company2);
        Page<Company> companyPage = new PageImpl<>(companyList);

        when(companyRepository.findAll(PageRequest.of(0, 10))).thenReturn(companyPage);
        when(companyMapper.toDto(any())).thenAnswer(invocation -> {
            Company company = invocation.getArgument(0);
            return company.getId() == 1 ? companyDto1 : companyDto2;
        });
        when(employeeFeignClient.getEmployeesByCompanyId(anyInt()))
                .thenReturn(List.of(new EmployeeDto()));

        Page<CompanyDto> actual = companyService.getAllCompanies(0, 10);

        assertNotNull(actual);
        assertEquals(2, actual.getTotalElements());

        verify(companyRepository).findAll(PageRequest.of(0, 10));
        verify(employeeFeignClient, times(2)).getEmployeesByCompanyId(anyInt());
    }

    @Test
    void test_getAllCompanies_emptyResult() {
        Page<Company> companyPage = Page.empty();

        when(companyRepository.findAll(PageRequest.of(0, 10))).thenReturn(companyPage);

        Page<CompanyDto> actual = companyService.getAllCompanies(0, 10);

        assertNotNull(actual);
        assertEquals(0, actual.getTotalElements());

        verify(companyRepository).findAll(PageRequest.of(0, 10));
        verifyNoInteractions(employeeFeignClient);
    }

    @Test
    void test_getAllCompanies_repositoryError() {
        when(companyRepository.findAll(PageRequest.of(0, 10))).thenThrow(new RuntimeException("Repository error"));

        assertThrows(RuntimeException.class, () -> companyService.getAllCompanies(0, 10));

        verify(companyRepository).findAll(PageRequest.of(0, 10));
        verifyNoInteractions(employeeFeignClient);
    }

    private static Stream<Arguments> provideCompanies() {
        return Stream.of(
                Arguments.of(1, "TechCorp", 1_000_000L, List.of(1, 2),
                        List.of(new EmployeeDto(1, "Иван", "Иванов", "+79161234567"),
                                new EmployeeDto(2, "Петр", "Петров", "+79161234568"))
                ),
                Arguments.of(2, "SoftSolutions", 500_000L, List.of(3),
                        Collections.emptyList()
                )
        );
    }

    private Company createCompany(int id, String name, long budget, List<Integer> employeeIds) {
        Company company = new Company(name, budget);
        company.setId(id);
        company.setEmployeeIds(employeeIds);
        return company;
    }

    private CompanyDto createCompanyDto(int id, String name, long budget, List<Integer> employeeIds) {
        return new CompanyDto(id, name, budget, employeeIds, new ArrayList<>());
    }
}