package isi.dan.ms_productos.modelo;
import jakarta.persistence.*;


import lombok.Data;

@Entity
@Table(name = "MS_PRD_CATEGORIA")
@Data
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "CATEGORIA_NOMBRE", nullable = false, unique = true)
    private String nombre;
}
