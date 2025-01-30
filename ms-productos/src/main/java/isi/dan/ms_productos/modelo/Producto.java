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
    private String nombre;
    private String descripcion;
    @Column(name ="STOCK_ACTUAL")
    private int stockActual;
    @Column(name ="STOCK_MINIMO")
    private int stockMinimo;
    private BigDecimal precio;
    @DecimalMin(value = "0.0")
    private BigDecimal descuentoPromocional = BigDecimal.ZERO; 
    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "CATEGORIA_ID", nullable = false) // Define la clave foránea
    private Categoria categoria;    //Habría que revisar si no es necesario cambiar un poco esto para que ponga explicitamente el nombre de la categoria

}
