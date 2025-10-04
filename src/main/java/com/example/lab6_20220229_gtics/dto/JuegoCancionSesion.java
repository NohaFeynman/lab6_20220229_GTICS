// src/main/java/com/example/lab6_20220229_gtics/dto/JuegoCancionSesion.java
package com.example.lab6_20220229_gtics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JuegoCancionSesion {
    private Long asignacionId;
    private String titulo;
    private Integer intentos = 0;
    private Integer letrasCorrectas = 0;
    private Integer posicionesCorrectas = 0;
    private Boolean adivinada = false;
}
