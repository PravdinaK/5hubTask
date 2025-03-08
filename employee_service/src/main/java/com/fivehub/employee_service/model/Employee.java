package com.fivehub.employee_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees",
        indexes = {@Index(name = "employees_last_name_index", columnList = "last_name"),
                @Index(name = "employees_company_id_index", columnList = "company_id")})
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "company_id", nullable = false)
    private int companyId;

    @Column(name = "phone_number", unique = true, nullable = true)
    private String phoneNumber;

    public Employee(String firstName, String lastName, int companyId, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyId = companyId;
        this.phoneNumber = phoneNumber;
    }
}