package isi.dan.ms_productos.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StockUpdateDTO {
    private Long idProducto;
    private Integer cantidad;
    private BigDecimal precio;


        // Getters y Setters
        public Long getId() {
            return idProducto;
        }
    
        public void setId(Long id) {
            this.idProducto = id;
        }
    
        public Integer getCantidad() {
            return cantidad;
        }
    
        public void setCantidad(Integer cant) {
            this.cantidad = cant;
        }
        public BigDecimal getPrecio(){
            return precio;
        }
        public void setPrecio(BigDecimal precio){
            this.precio = precio;
        }
}
