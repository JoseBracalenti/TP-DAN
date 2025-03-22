package isi.dan.msclientes.utils;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.msclientes.dao.ClienteRepository;
import isi.dan.msclientes.dto.CreateObraDTO;
import isi.dan.msclientes.dto.ObraDTO;
import isi.dan.msclientes.dto.UpdateObraDTO;
import isi.dan.msclientes.exception.ClienteNotFoundException;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.EstadoDeObra;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.servicios.EstadoDeObraService;

@Service
public class ObraMapper {

    @Autowired
    private EstadoDeObraService estadoDeObraService;

    @Autowired
    private ClienteRepository clienteRepository;

    public Obra toEntity(ObraDTO dto) {
        if (dto == null) {
            return null;
        }
        Obra obra = new Obra();
        obra.setId(dto.getId());
        obra.setDireccion(dto.getDireccion());
        obra.setEsRemodelacion(dto.getEsRemodelacion());
        obra.setLat(dto.getLat());
        obra.setLng(dto.getLng());
        obra.setCliente(ClienteMapper.toEntity(dto.getCliente()));
        obra.setPresupuesto(dto.getPresupuesto());

        EstadoDeObra estado = estadoDeObraService.findByEstado(dto.getEstado());
        obra.setEstado(estado);

        return obra;
    }

    public static ObraDTO toDTO(Obra obra) {
        if (obra == null) {
            return null;
        }
        ObraDTO dto = new ObraDTO();
        dto.setId(obra.getId());
        dto.setDireccion(obra.getDireccion());
        dto.setEsRemodelacion(obra.getEsRemodelacion());
        dto.setLat(obra.getLat());
        dto.setLng(obra.getLng());
        dto.setEstado(String.valueOf(obra.getEstado().getEstado()));
        dto.setCliente(ClienteMapper.toDTO(obra.getCliente()));
        dto.setPresupuesto(obra.getPresupuesto());
        return dto;
    }

    public Obra toEntity(UpdateObraDTO dto) throws NoSuchElementException {
        if (dto == null) {
            return null;
        }
        Obra obra = new Obra();
        obra.setId(dto.getId());
        obra.setDireccion(dto.getDireccion());
        obra.setEsRemodelacion(dto.getEsRemodelacion());
        obra.setLat(dto.getLat());
        obra.setLng(dto.getLng());
        obra.setPresupuesto(dto.getPresupuesto());
        EstadoDeObra estado = estadoDeObraService.findByEstado(dto.getEstado());
        obra.setEstado(estado);
        return obra;
    }

    public Obra toEntity(CreateObraDTO dto) throws ClienteNotFoundException {
        if (dto == null) {
            return null;
        }
        Obra obra = new Obra();
        obra.setDireccion(dto.getDireccion());
        obra.setEsRemodelacion(dto.getEsRemodelacion());
        obra.setLat(dto.getLat());
        obra.setLng(dto.getLng());
        obra.setPresupuesto(dto.getPresupuesto());
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ClienteNotFoundException("Cliente " + dto.getClienteId() + " no encontrado"));
        obra.setCliente(cliente);
        return obra;
    }

    public Obra toEntity(CreateObraDTO dto, Cliente cliente) {
        if (dto == null) {
            return null;
        }
        Obra obra = new Obra();
        obra.setDireccion(dto.getDireccion());
        obra.setEsRemodelacion(dto.getEsRemodelacion());
        obra.setLat(dto.getLat());
        obra.setLng(dto.getLng());
        obra.setPresupuesto(dto.getPresupuesto());
        obra.setCliente(cliente);
        return obra;
    }

}
