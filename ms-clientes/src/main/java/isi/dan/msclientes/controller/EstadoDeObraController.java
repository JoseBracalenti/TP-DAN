package isi.dan.msclientes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isi.dan.msclientes.dto.EstadoDeObraDTO;
import isi.dan.msclientes.model.EstadoDeObra;
import isi.dan.msclientes.servicios.EstadoDeObraService;

@RestController
@RequestMapping("/estado-de-obra")
public class EstadoDeObraController {

    @Autowired
    private EstadoDeObraService estadoDeObraService;

    @GetMapping
    public ResponseEntity<List<EstadoDeObra>> getAll() {
        return ResponseEntity.ok(estadoDeObraService.findAll());
    }

    @PostMapping
    public ResponseEntity<EstadoDeObra> create(@RequestBody @Validated EstadoDeObraDTO estado) {
        return ResponseEntity.ok(estadoDeObraService.create(estado.getEstado()));
    }

}
