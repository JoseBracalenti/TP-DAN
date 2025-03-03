package isi.dan.msclientes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import isi.dan.msclientes.aop.LogExecutionTime;
import isi.dan.msclientes.dto.CreateObraDTO;
import isi.dan.msclientes.dto.ObraDTO;
import isi.dan.msclientes.dto.UpdateObraDTO;
import isi.dan.msclientes.exception.ClienteNotFoundException;
import isi.dan.msclientes.exception.ObraNotFoundException;
import isi.dan.msclientes.servicios.ObraService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/obras")
public class ObraController {

    @Autowired
    private ObraService obraService;

    @GetMapping
    @LogExecutionTime
    public List<ObraDTO> getAll() {
        return obraService.findAll();
    }

    @GetMapping("/{id}")
    @LogExecutionTime
    public ResponseEntity<ObraDTO> getById(@PathVariable Integer id) throws ObraNotFoundException {
        return ResponseEntity.ok(obraService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ObraDTO> create(@RequestBody CreateObraDTO obra)
            throws ClienteNotFoundException {
        return ResponseEntity.ok(obraService.save(obra));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObraDTO> update(@PathVariable Integer id, @RequestBody @Validated UpdateObraDTO obra)
            throws ObraNotFoundException, NoSuchElementException {
        return ResponseEntity.ok(obraService.update(obra));
    }

    @PutMapping("/{id}/habilitar")
    public ResponseEntity<Void> habilitarObra(@PathVariable Integer id) throws ObraNotFoundException {
        obraService.habilitarObra(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/pendiente")
    public ResponseEntity<Void> pendienteObra(@PathVariable Integer id) throws ObraNotFoundException {
        obraService.pendienteObra(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarObra(@PathVariable Integer id) throws ObraNotFoundException {
        obraService.finalizarObra(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws ObraNotFoundException {
        obraService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
