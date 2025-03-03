package isi.dan.msclientes.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateClienteDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "Email debe ser válido")
    @NotBlank(message = "Email es obligatorio")
    private String correoElectronico;

    @NotBlank
    @Pattern(regexp = "^\\d{2}-\\d{1,8}-\\d{1}$", message = "CUIT inválido")
    private String cuit;

    @Min(value = 10000, message = "El descubierto máximo debe ser al menos 10000")
    private BigDecimal maximoDescubierto;
}
