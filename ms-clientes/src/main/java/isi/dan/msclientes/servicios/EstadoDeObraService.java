package isi.dan.msclientes.servicios;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.msclientes.dao.EstadoDeObraRepository;
import isi.dan.msclientes.model.EstadoDeObra;

@Service
public class EstadoDeObraService {
    @Autowired
    private EstadoDeObraRepository estadoDeObraRepository;

    public EstadoDeObra create(String estado) {
        EstadoDeObra estadoDeObra = new EstadoDeObra();
        estadoDeObra.setEstado(estado);
        return estadoDeObraRepository.save(estadoDeObra);
    }

    public EstadoDeObra findByEstado(String estado) throws NoSuchElementException{
        return estadoDeObraRepository.findByEstado(estado)
                .orElseThrow(() -> new NoSuchElementException("Estado " + estado + " no encontrado"));
    }

}
