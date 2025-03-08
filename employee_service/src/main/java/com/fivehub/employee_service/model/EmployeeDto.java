package com.fivehub.employee_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о работнике")
public class EmployeeDto {
    @Schema(description = "ID работника", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Schema(description = "Имя работника")
    private String firstName;

    @Schema(description = "Фамилия работника")
    private String lastName;

    @Schema(description = "Номер телефона работника")
    private String phoneNumber;
}