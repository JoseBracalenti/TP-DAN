package isi.dan.msclientes.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.msclientes.dao.ObraRepository;
import isi.dan.msclientes.dto.ObraDTO;
import isi.dan.msclientes.dto.UpdateObraDTO;
import isi.dan.msclientes.exception.ObraNotFoundException;
import isi.dan.msclientes.utils.ObraMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObraService {

    @Autowired
    private ObraRepository obraRepository;

    public List<ObraDTO> findAll() {
        return obraRepository.findAll().stream().map(ObraMapper::toDTO).collect(Collectors.toList());
    }

    public ObraDTO findById(Integer id) throws ObraNotFoundException {
        return ObraMapper.toDTO(obraRepository.findById(id)
                .orElseThrow(() -> new ObraNotFoundException("Obra " + id + " no encontrado")));
    }

    public ObraDTO save(ObraDTO obra) {
        return ObraMapper.toDTO(obraRepository.save(ObraMapper.toEntity(obra)));
    }

    public ObraDTO update(UpdateObraDTO obra) throws ObraNotFoundException {
        obraRepository.findById(obra.getId()).orElseThrow(() -> new ObraNotFoundException("Obra " + obra.getId() + " no encontrado para actualizar"));
        return ObraMapper.toDTO(obraRepository.save(ObraMapper.toEntity(obra)));
    }

    public void deleteById(Integer id) throws ObraNotFoundException {
        obraRepository.findById(id).orElseThrow(() -> new ObraNotFoundException("Obra " + id + " no encontrado para borrar"));
        obraRepository.deleteById(id);
    }
}
