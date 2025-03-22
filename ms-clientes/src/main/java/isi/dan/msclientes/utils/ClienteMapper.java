package isi.dan.msclientes.utils;

import isi.dan.msclientes.dto.ClienteDTO;
import isi.dan.msclientes.model.Cliente;

// Esto deberia ser MapStruct, no una implementacion a pata,
// para que sea más escalable y mantenible
public class ClienteMapper {

    public static Cliente toEntity(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setNombre(dto.getNombre());
        cliente.setCorreoElectronico(dto.getCorreoElectronico());
        cliente.setCuit(dto.getCuit());
        cliente.setMaximoDescubierto(dto.getMaximoDescubierto());
        return cliente;
    }

    public static ClienteDTO toDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setCorreoElectronico(cliente.getCorreoElectronico());
        dto.setCuit(cliente.getCuit());
        dto.setMaximoDescubierto(cliente.getMaximoDescubierto());
        return dto;
    }

    public static void updateClienteFromDTO(ClienteDTO dto, Cliente entity) {
        if (dto.getNombre() != null)
            entity.setNombre(dto.getNombre());
        if (dto.getCorreoElectronico() != null)
            entity.setCorreoElectronico(dto.getCorreoElectronico());
        if (dto.getCuit() != null)
            entity.setCuit(dto.getCuit());
        if (dto.getMaximoDescubierto() != null)
            entity.setMaximoDescubierto(dto.getMaximoDescubierto());
    }

}
