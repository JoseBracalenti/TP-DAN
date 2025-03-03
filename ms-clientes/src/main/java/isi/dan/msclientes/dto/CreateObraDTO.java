package isi.dan.msclientes.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateObraDTO {

    @NotEmpty(message = "La dirección no puede estar vacía")
    @Size(min = 1, max = 100)
    private String direccion;

    private Boolean esRemodelacion;

    @Digits(integer = 9, fraction = 6, message = "La latitud debe tener hasta 9 dígitos enteros y 6 decimales")
    private float lat;

    @Digits(integer = 9, fraction = 6, message = "La longitud debe tener hasta 9 dígitos enteros y 6 decimales")
    private float lng;

    @NotNull
    private Integer clienteId;

    @PositiveOrZero(message = "El presupuesto no puede ser negativo")
    private BigDecimal presupuesto;

}
