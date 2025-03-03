package isi.dan.msclientes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "MS_CLI_USER")
@Data
public class User {
    // nombre, el apellido, el dni y el correo electrónico.
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String dni;

    @Column(name="CORREO_ELECTRONICO", unique = true)
    private String correoElectronico;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private Cliente cliente;
}
