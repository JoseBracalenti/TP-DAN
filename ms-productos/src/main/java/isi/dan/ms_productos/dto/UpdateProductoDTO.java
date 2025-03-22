package isi.dan.ms_productos.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;


public class UpdateProductoDTO {

   
    private String nombre;
   
    private String descripcion;
   
    @Min(value = 0)
    private int stockMinimo;
    private int stockActual;
    
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precio;
    
    
    @DecimalMin(value = "0.0")
    private BigDecimal descuentoPromocional = BigDecimal.ZERO; 
    
    private String nombreCategoria; // Sólo el nombre de la categoria


    // Getters y Setters

    public int getStockActual(){
        return stockActual;
    }
    public void setStockActual(int stockActual){
        this.stockActual = stockActual;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getDescuentoPromocional() {
        return descuentoPromocional;
    }

    public void setDescuentoPromocional(BigDecimal descuentoPromocional) {
        this.descuentoPromocional = descuentoPromocional;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

}


