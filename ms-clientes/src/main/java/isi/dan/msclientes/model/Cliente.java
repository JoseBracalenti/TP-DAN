package isi.dan.msclientes.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "MS_CLI_CLIENTE")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "CORREO_ELECTRONICO", unique = true)
    private String correoElectronico;

    @Column(nullable = false, unique = true)
    private String cuit;

    @Column(name = "MAXIMO_DESCUBIERTO", nullable = false)
    private BigDecimal maximoDescubierto;
}
