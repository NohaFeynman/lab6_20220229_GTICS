package com.example.lab6_20220229_gtics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IntencionDto {
    @NotBlank(message = "Ingrese una descripción")
    @Size(min = 15, message = "Mínimo 15 caracteres")
    private String descripcion;
}
