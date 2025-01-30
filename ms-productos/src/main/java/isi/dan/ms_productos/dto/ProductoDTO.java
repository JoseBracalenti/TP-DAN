package isi.dan.ms_productos.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductoDTO {

    @NotNull
    private String nombre;
    @NotNull
    private String descripcion;
    @NotNull
    @Min(value = 0)
    private int stockMinimo;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precioInicial;
    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal descuentoPromocional = BigDecimal.ZERO; 
    @NotNull
    private String nombreCategoria; // Sólo el nombre de la categoria

}

