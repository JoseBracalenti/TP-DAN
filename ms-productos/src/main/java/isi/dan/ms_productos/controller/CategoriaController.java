package isi.dan.ms_productos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import isi.dan.ms_productos.aop.LogExecutionTime;
import isi.dan.ms_productos.exception.CategoriaNotFoundException;
import isi.dan.ms_productos.dto.*;
import isi.dan.ms_productos.servicio.EchoClientFeign;
import isi.dan.ms_productos.servicio.CategoriaService;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    Logger log = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    EchoClientFeign echoSvc;

    @PostMapping
    @LogExecutionTime
    public ResponseEntity<CategoriaDTO> createCategoriaByName(@RequestBody @Validated CategoriaDTO categoriaDTO) {
        return ResponseEntity.ok(categoriaService.newCategoria(categoriaDTO));
    }

    @PutMapping("/{nombre}")
    @LogExecutionTime
    public ResponseEntity<UpdateCategoriaDTO> updateCategoria(@RequestBody @Validated UpdateCategoriaDTO updateCategoriaDTO,@PathVariable String nombre) throws CategoriaNotFoundException {
        return ResponseEntity.ok(categoriaService.updateCategoria(updateCategoriaDTO, nombre));
    }

    @GetMapping
    @LogExecutionTime
    public ResponseEntity<List<CategoriaDTO>> getAllCategorias() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }

    @GetMapping("/{nombre}")
    @LogExecutionTime
    public ResponseEntity<CategoriaDTO> getCategoriaByName(@PathVariable String nombre) throws CategoriaNotFoundException {// getcategoria tira la excp
        return  ResponseEntity.ok(categoriaService.getCategoriaByName(nombre));
    }
       
    @DeleteMapping("/{nombre}")
    @LogExecutionTime
    public ResponseEntity<Void> deleteCategoria(@PathVariable String nombre) {
        categoriaService.deleteCategoria(nombre);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/test")
    @LogExecutionTime
    public String getEcho() {
        String resultado = echoSvc.echo();
        log.info("Log en test 1!!!! {}",resultado);
        return resultado;
    }

    @GetMapping("/test2")
    @LogExecutionTime
    public String getEcho2() {
        RestTemplate restTemplate = new RestTemplate();
        String gatewayURL = "http://ms-gateway-svc:8080";
        String resultado = restTemplate.getForObject(gatewayURL+"/clientes/api/clientes/echo", String.class);
        log.info("Log en test 2 {}",resultado);
        return resultado;
    }

}

