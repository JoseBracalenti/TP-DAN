package isi.dan.ms.pedidos.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import isi.dan.ms.pedidos.modelo.Cliente;
import isi.dan.ms.pedidos.modelo.DetallePedido;
import isi.dan.ms.pedidos.modelo.EstadoPedido;

public class PedidoDTO {
    private String id;
    private Instant fecha;
    private Integer numeroPedido;
    private String usuario;
    private String observaciones;
    private EstadoPedido Estado;
    private ClienteDTO clienteDTO;
    private BigDecimal total;
    private List<DetallePedido> detalle;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Instant getFecha() {
        return fecha;
    }
    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }
    public Integer getNumeroPedido() {
        return numeroPedido;
    }
    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public EstadoPedido getEstado() {
        return Estado;
    }
    public void setEstado(EstadoPedido estado) {
        Estado = estado;
    }
    public ClienteDTO getCliente() {
        return clienteDTO;
    }
    public void setCliente(ClienteDTO cliente) {
        this.clienteDTO = cliente;
    }
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    public List<DetallePedido> getDetalle() {
        return detalle;
    }
    public void setDetalle(List<DetallePedido> detalle) {
        this.detalle = detalle;
    }
    
}
