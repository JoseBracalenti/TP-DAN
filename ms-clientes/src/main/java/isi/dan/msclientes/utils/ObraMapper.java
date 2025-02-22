package isi.dan.msclientes.utils;

import org.springframework.beans.factory.annotation.Autowired;

import isi.dan.msclientes.dao.EstadoDeObraRepository;
import isi.dan.msclientes.dto.ObraDTO;
import isi.dan.msclientes.dto.UpdateObraDTO;
import isi.dan.msclientes.model.EstadoDeObra;
import isi.dan.msclientes.model.Obra;

public class ObraMapper {

    @Autowired
    private static EstadoDeObraRepository estadoDeObraRepository;

    public static Obra toEntity(ObraDTO dto) {
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

        EstadoDeObra estado = estadoDeObraRepository.findByEstado(dto.getEstado());
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

    public static Obra toEntity(UpdateObraDTO dto) {
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
        EstadoDeObra estado = estadoDeObraRepository.findByEstado(dto.getEstado());
        obra.setEstado(estado);
        return obra;
    }

}
