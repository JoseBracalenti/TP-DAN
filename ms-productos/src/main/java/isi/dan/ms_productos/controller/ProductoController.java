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
import isi.dan.ms_productos.exception.ProductoNotFoundException;

import isi.dan.ms_productos.dto.*;
import isi.dan.ms_productos.servicio.EchoClientFeign;
import isi.dan.ms_productos.servicio.ProductoService;

import java.math.BigDecimal;
import java.util.List;




@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    Logger log = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    EchoClientFeign echoSvc;

    // Create, update, get ,getAll y delete, punto b
    @PostMapping
    @LogExecutionTime
    public ResponseEntity<ProductoDTO> createProducto(@RequestBody @Validated ProductoDTO productoDTO) throws CategoriaNotFoundException {
        return ResponseEntity.ok(productoService.newProducto(productoDTO));
    }

    @PutMapping
    @LogExecutionTime
    public ResponseEntity<ProductoDTO> updateProducto(@RequestBody @Validated ProductoDTO productoDTO) throws CategoriaNotFoundException {
        return ResponseEntity.ok(productoService.saveProducto(productoDTO));
    }


    @GetMapping
    @LogExecutionTime
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }
 

    @DeleteMapping("/{id}")
    @LogExecutionTime
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) throws ProductoNotFoundException{
       productoService.deleteProducto(id);
       return ResponseEntity.noContent().build(); 
    }
   
    @GetMapping("/{id}")
    @LogExecutionTime
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) throws ProductoNotFoundException {
        return  ResponseEntity.ok(productoService.getProductoById(id));

    }


    // Actualizar stock, punto c.
    @PutMapping("{id}/actualizar_stock")
    @LogExecutionTime
    public ResponseEntity<ProductoDTO> actualizarStock(@PathVariable Long id,@RequestBody @Validated StockUpdateDTO stockUpdateDTO) throws ProductoNotFoundException  {
        return ResponseEntity.ok(productoService.updateProductoStock(id,stockUpdateDTO));
    }

    // Actualizar descuento por promoción, punto d.
    @PutMapping("{id}/{desc_promo}")
    @LogExecutionTime
    public ResponseEntity<ProductoDTO> actualizarDescuento(@PathVariable @Validated Long id, @PathVariable @Validated BigDecimal desc_promo ) throws ProductoNotFoundException{
        return ResponseEntity.ok(productoService.updateDescuento(id,desc_promo)) ;
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

