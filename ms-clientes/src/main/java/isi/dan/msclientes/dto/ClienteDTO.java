package isi.dan.msclientes.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteDTO {
    @Null
    private Integer id;

    @Size(min = 1, max = 100)
    private String nombre;

    @Email(message = "Email debe ser válido")
    private String correoElectronico;

    @Pattern(regexp = "^\\d{2}-\\d{1,8}-\\d{1}$", message = "CUIT inválido")
    private String cuit;

    @Min(value = 10000, message = "El descubierto máximo debe ser al menos 10000")
    private BigDecimal maximoDescubierto;

    @Null
    private List<ObraDTO> obras;
}
