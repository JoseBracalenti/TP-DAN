package isi.dan.ms.pedidos.dto;

import java.math.BigDecimal;

public class ClienteDTO {
    private Integer id;
    private String nombre;
    private String correoElectronico;
    private String cuit;
    private BigDecimal maximoDescubierto;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
    public String getCuit() {
        return cuit;
    }
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
    public BigDecimal getMaximoDescubierto() {
        return maximoDescubierto;
    }
    public void setMaximoDescubierto(BigDecimal maximoDescubierto) {
        this.maximoDescubierto = maximoDescubierto;
    }

    
}
