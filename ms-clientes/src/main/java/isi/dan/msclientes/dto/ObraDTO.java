package isi.dan.msclientes.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ObraDTO {

    private Integer id;

    private String direccion;

    private Boolean esRemodelacion;

    private float lat;

    private float lng;

    private String estado;

    private ClienteDTO cliente;

    private BigDecimal presupuesto;
}
// No @Validte should be applied, because it's going to be used to return.
// CreateObra should be handled by other DTO.