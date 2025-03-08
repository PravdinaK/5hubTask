package com.fivehub.company_service.initializer;

import com.fivehub.company_service.model.Company;
import com.fivehub.company_service.repository.CompanyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CompanyDatabaseInitializer implements CommandLineRunner {

    private final CompanyRepository companyRepository;

    public CompanyDatabaseInitializer(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (companyRepository.count() == 0) {
            Company company1 = new Company("ТехКорп", 5000000L);
            Company company2 = new Company("Инноватикс", 10000000L);

            companyRepository.save(company1);
            companyRepository.save(company2);
            System.out.println("Данные о компаниях были успешно добавлены.");
        }
    }
}