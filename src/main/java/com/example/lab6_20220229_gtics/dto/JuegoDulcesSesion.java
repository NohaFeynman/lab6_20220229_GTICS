package com.example.lab6_20220229_gtics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JuegoDulcesSesion {
    private Long asignacionId;
    private Integer objetivo;
    private Integer intentos = 0;
    private Integer minimoPasos;
    private Boolean adivinado = false;
}
