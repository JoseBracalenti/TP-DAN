package isi.dan.msclientes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import isi.dan.msclientes.aop.LogExecutionTime;
import isi.dan.msclientes.dto.ClienteDTO;
import isi.dan.msclientes.dto.CreateClienteDTO;
import isi.dan.msclientes.exception.ClienteNotFoundException;
import isi.dan.msclientes.servicios.ClienteService;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    Logger log = LoggerFactory.getLogger(ClienteController.class);

    @Value("${dan.clientes.instancia}")
    private String instancia;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @LogExecutionTime
    public ResponseEntity<List<ClienteDTO>> getAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/echo")
    @LogExecutionTime
    public String getEcho() {
        log.debug("Recibiendo un echo ----- {}", instancia);
        return Instant.now() + " - " + instancia;
    }

    @GetMapping("/{id}")
    @LogExecutionTime
    public ResponseEntity<ClienteDTO> getById(@PathVariable Integer id) throws ClienteNotFoundException {
        ClienteDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    @LogExecutionTime
    public ResponseEntity<ClienteDTO> create(@RequestBody @Validated CreateClienteDTO cliente) {
        return ResponseEntity.ok(clienteService.save(cliente));
    };

    @PutMapping("/{id}")
    @LogExecutionTime
    public ResponseEntity<ClienteDTO> update(@PathVariable final Integer id, @RequestBody @Validated ClienteDTO cliente)
            throws ClienteNotFoundException {
        cliente.setId(id);
        return ResponseEntity.ok(clienteService.update(id, cliente));
    }

    @DeleteMapping("/{id}")
    @LogExecutionTime
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws ClienteNotFoundException {
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
