package isi.dan.ms.pedidos.dto;

import java.util.List;

public class CrearPedidoDTO {
    private List<ProductoPedidoDTO> productos;
    private String observaciones;
    private Integer obraId;
    private Integer clienteId;

    public List<ProductoPedidoDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoPedidoDTO> productos) {
        this.productos = productos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getObraId() {
        return obraId;
    }

    public void setObraId(Integer obraId) {
        this.obraId = obraId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public static class ProductoPedidoDTO {
        private Long id;
        private Integer cantidad;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}
