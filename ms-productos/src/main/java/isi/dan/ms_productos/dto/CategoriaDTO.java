package isi.dan.ms_productos.dto;


import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CategoriaDTO {

    @Id
    private Long id;
    @NotNull
    private String nombre;


}

