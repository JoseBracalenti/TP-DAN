package isi.dan.ms_productos.modelo;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;


@Entity
@Table(name = "MS_PRD_PRODUCTO")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column
    private String nombre;
    @Column
    private String descripcion;
    @Column(name ="STOCK_ACTUAL")
    private int stockActual;
    @Column(name ="STOCK_MINIMO")
    private int stockMinimo;
    @Column
    private BigDecimal precio;
    @Column
    @DecimalMin(value = "0.0")
    private BigDecimal descuentoPromocional = BigDecimal.ZERO; 
    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "CATEGORIA_NOMBRE", nullable = false) // Define la clave foránea
    private Categoria categoria;    

}
