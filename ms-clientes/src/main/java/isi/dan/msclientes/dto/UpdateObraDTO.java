package isi.dan.msclientes.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateObraDTO {

    @Null
    private Integer id;

    @NotEmpty(message = "La dirección no puede estar vacía")
    @Size(min = 1, max = 100)
    private String direccion;

    private Boolean esRemodelacion;

    @Digits(integer = 9, fraction = 6, message = "La latitud debe tener hasta 9 dígitos enteros y 6 decimales")
    private float lat;

    @Digits(integer = 9, fraction = 6, message = "La longitud debe tener hasta 9 dígitos enteros y 6 decimales")
    private float lng;

    @Null(message = "Utilice los endpoints especificos para actualizar el estado de una obra.")
    private String estado;

    @PositiveOrZero(message = "El presupuesto no puede ser negativo")
    private BigDecimal presupuesto;
}
