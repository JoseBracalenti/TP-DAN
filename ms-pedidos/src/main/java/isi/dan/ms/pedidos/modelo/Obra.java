
package isi.dan.ms.pedidos.modelo;

import java.math.BigDecimal;
public class Obra {

    private Integer id;
    private String direccion;
    private Boolean esRemodelacion;
    private float lat;
    private float lng;
    private Cliente cliente;
    private BigDecimal presupuesto;
    public Integer getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    public Boolean getEsRemodelacion() {
        return esRemodelacion;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public void setEsRemodelacion(Boolean esRemodelacion) {
        this.esRemodelacion = esRemodelacion;
    }
    public void setLat(float lat) {
        this.lat = lat;
    }
    public void setLng(float lng) {
        this.lng = lng;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }
    
}
