package isi.dan.ms_productos.dto;


import jakarta.validation.constraints.*;

public class CategoriaDTO {

    private Long id;
    @NotNull
    private String nombre;
    
    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String name) {
        this.nombre = name;
    }
}




