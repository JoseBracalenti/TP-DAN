package isi.dan.msclientes.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import isi.dan.msclientes.dao.EstadoDeObraRepository;
import isi.dan.msclientes.dao.ObraRepository;
import isi.dan.msclientes.dto.CreateObraDTO;
import isi.dan.msclientes.dto.ObraDTO;
import isi.dan.msclientes.dto.UpdateObraDTO;
import isi.dan.msclientes.enums.EstadoDeObraEnum;
import isi.dan.msclientes.exception.ClienteNotFoundException;
import isi.dan.msclientes.exception.ObraNotFoundException;
import isi.dan.msclientes.model.EstadoDeObra;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.utils.ObraMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ObraService {

    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private ObraMapper obraMapper;

    @Autowired
    private EstadoDeObraRepository estadoDeObraRepository;

    @Value("${maximo.obras}")
    private Integer maximoCantidadDeObras;

    public List<ObraDTO> findAll() {
        return obraRepository.findAll().stream().map(ObraMapper::toDTO).collect(Collectors.toList());
    }

    public ObraDTO findById(Integer id) throws ObraNotFoundException {
        return ObraMapper.toDTO(obraRepository.findById(id)
                .orElseThrow(() -> new ObraNotFoundException("Obra " + id + " no encontrado")));
    }

    public List<ObraDTO> findByClienteId(Integer id) {
        return obraRepository.findByClienteId(id).stream().map(ObraMapper::toDTO).collect(Collectors.toList());
    }

    public ObraDTO save(CreateObraDTO dto) throws ClienteNotFoundException {

        Obra obra = obraMapper.toEntity(dto);

        List<Obra> obrasActivas = obraRepository.findByClienteIdAndEstado_Estado(dto.getClienteId(), "HABILITADA");

        if (obrasActivas.size() >= maximoCantidadDeObras) {
            asignarEstado(obra, "PENDIENTE");
        } else {
            asignarEstado(obra, "HABILITADA");
        }

        return ObraMapper.toDTO(obraRepository.save(obra));
    }

    public void asignarEstado(Obra obra, String estado) {
        EstadoDeObra estadoDeObra = estadoDeObraRepository.findByEstado(estado).orElseThrow(
                () -> new NoSuchElementException("Estado " + estado + " no encontrado"));
        obra.setEstado(estadoDeObra);
        obraRepository.save(obra);
    }

    public ObraDTO update(Integer id, UpdateObraDTO obra) throws ObraNotFoundException, NoSuchElementException {
        Obra obraActual = obraRepository.findById(id).orElseThrow(
                () -> new ObraNotFoundException("Obra " + obra.getId() + " no encontrado para actualizar"));
        EstadoDeObraEnum estadoEnum = EstadoDeObraEnum.valueOf(obra.getEstado());

        switch (estadoEnum) {
            case HABILITADA:
                habilitarObra(obraActual.getId());
                break;
            case PENDIENTE:
                pendienteObra(obraActual.getId());
                break;
            case FINALIZADA:
                finalizarObra(obraActual.getId());
                break;
        }

        obraActual.setDireccion(obra.getDireccion());
        obraActual.setEsRemodelacion(obra.getEsRemodelacion());
        obraActual.setLat(obra.getLat());
        obraActual.setLng(obra.getLng());
        obraActual.setPresupuesto(obra.getPresupuesto());

        return ObraMapper.toDTO(obraRepository.save(obraActual));
    }

    public void deleteById(Integer id) throws ObraNotFoundException {
        obraRepository.findById(id)
                .orElseThrow(() -> new ObraNotFoundException("Obra " + id + " no encontrado para borrar"));
        obraRepository.deleteById(id);
    }

    public void habilitarObra(Integer id) throws ObraNotFoundException {
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new ObraNotFoundException("Obra " + id + " no encontrado"));

        String estadoActual = obra.getEstado().getEstado();

        if (estadoActual.equals("HABILITADA")) {
            return;
        }

        if (estadoActual.equals("FINALIZADA")) {
            throw new IllegalArgumentException("La obra ya está finalizada.");
        }

        List<Obra> obrasActivas = obraRepository.findByClienteIdAndEstado_Estado(obra.getCliente().getId(),
                "HABILITADA");

        if (obrasActivas.size() >= maximoCantidadDeObras) {
            throw new IllegalArgumentException("Ya hay " + maximoCantidadDeObras + " obras habilitadas.");
        }

        asignarEstado(obra, "HABILITADA");

        obraRepository.save(obra);
    }

    public void pendienteObra(Integer id) throws ObraNotFoundException {
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new ObraNotFoundException("Obra " + id + " no encontrado"));

        String estadoActual = obra.getEstado().getEstado();

        if (estadoActual.equals("PENDIENTE")) {
            return;
        }

        if (estadoActual.equals("FINALIZADA")) {
            throw new IllegalArgumentException("La obra ya está finalizada.");
        }

        asignarEstado(obra, "PENDIENTE");

        obraRepository.save(obra);
    }

    public void finalizarObra(Integer id) throws ObraNotFoundException {
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new ObraNotFoundException("Obra " + id + " no encontrado"));

        if (obra.getEstado().getEstado().equals("FINALIZADA")) {
            return;
        }

        asignarEstado(obra, "FINALIZADA");

        obraRepository.save(obra);

        List<Obra> obrasPendientes = obraRepository.findByClienteIdAndEstado_Estado(obra.getCliente().getId(),
                "PENDIENTE");

        Logger logger = Logger.getLogger(ObraService.class.getName());
        logger.log(Level.INFO, "Obras pendientes: {0}", obrasPendientes);

        if (obrasPendientes.size() > 0) {
            asignarEstado(obrasPendientes.get(0), "HABILITADA");
            obraRepository.save(obrasPendientes.get(0));
        }
    }
}
