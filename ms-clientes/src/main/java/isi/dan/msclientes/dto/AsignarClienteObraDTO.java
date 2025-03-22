package isi.dan.msclientes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsignarClienteObraDTO {
    @NotNull(message = "El id del cliente es requerido")
    @Min(value = 1)
    private Integer idCliente;
}
