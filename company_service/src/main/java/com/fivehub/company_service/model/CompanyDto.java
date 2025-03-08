package com.fivehub.company_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о компании")
public class CompanyDto {

    @Schema(description = "ID компании", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Schema(description = "Название компании", example = "Tech Corp")
    private String name;

    @Schema(description = "Бюджет компании", example = "1000000")
    private long budget;

    @Schema(description = "Список идентификаторов сотрудников")
    private List<Integer> employeeIds;

    @Schema(description = "Список сотрудников", accessMode = Schema.AccessMode.READ_ONLY)
    private List<EmployeeDto> employees;
}