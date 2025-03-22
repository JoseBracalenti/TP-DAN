package isi.dan.msclientes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EstadoDeObraDTO {
    @NotBlank
    private String estado;
}
