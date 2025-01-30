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
import isi.dan.ms_productos.modelo.Categoria;
import isi.dan.ms_productos.dto.CategoriaDTO;
import isi.dan.ms_productos.servicio.EchoClientFeign;
import isi.dan.ms_productos.servicio.CategoriaService;

import isi.dan.ms_productos.utils.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    Logger log = LoggerFactory.getLogger(CategoriaController.class);
    private Mapper mapper;

    @Autowired
    EchoClientFeign echoSvc;

    @PostMapping
    @LogExecutionTime
    public ResponseEntity<CategoriaDTO> createCategoriaByName(@RequestBody @Validated CategoriaDTO categoriaDTO) {


        return ResponseEntity.ok(mapper.categoriaToDTO(categoriaService.newCategoria(categoriaDTO)));
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

    @GetMapping
    @LogExecutionTime
    public List<Categoria> getAllCategorias() {
        return categoriaService.getAllCategorias();
    }

    @GetMapping("/{name}")
    @LogExecutionTime
    public ResponseEntity<Categoria> getCategoriaByName(@PathVariable String name) throws CategoriaNotFoundException {
        return  ResponseEntity.ok(categoriaService.getCategoriaByName(name));
    }
       
    @DeleteMapping("/{name}")
    @LogExecutionTime
    public ResponseEntity<Void> deleteCategoria(@PathVariable String name) {
        categoriaService.deleteCategoria(name);
        return ResponseEntity.noContent().build();
    }


}

