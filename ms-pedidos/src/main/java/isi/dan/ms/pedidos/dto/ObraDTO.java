package isi.dan.ms.pedidos.dto;

import java.math.BigDecimal;

import isi.dan.ms.pedidos.modelo.Cliente;

public class ObraDTO {
private Integer id;
    private String direccion;
    private Boolean esRemodelacion;
    private float lat;
    private float lng;
    private ClienteDTO cliente;
    private BigDecimal presupuesto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getEsRemodelacion() {
        return esRemodelacion;
    }

    public void setEsRemodelacion(Boolean esRemodelacion) {
        this.esRemodelacion = esRemodelacion;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

}
