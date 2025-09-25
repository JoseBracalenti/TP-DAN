package isi.dan.ms.pedidos.dto;

import java.math.BigDecimal;

public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private int stockMinimo;
    private BigDecimal precio;
    private BigDecimal descuentoPromocional;
    private String nombreCategoria;

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