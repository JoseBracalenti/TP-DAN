package isi.dan.ms_productos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import isi.dan.ms_productos.dto.*;
import isi.dan.ms_productos.exception.CategoriaNotFoundException;
import isi.dan.ms_productos.exception.ProductoNotFoundException;
import isi.dan.ms_productos.servicio.EchoClientFeign;
import isi.dan.ms_productos.servicio.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private EchoClientFeign echoSvc;

    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setNombre("Test Producto");
        productoDTO.setDescripcion("Descripción de prueba");
        productoDTO.setStockMinimo(2);
        productoDTO.setPrecio(new BigDecimal("100.00"));
        productoDTO.setDescuentoPromocional(new BigDecimal("0.0"));
        productoDTO.setNombreCategoria("TestCategoria");
    }

    @Test
    void testCreateProducto() throws Exception {
        Mockito.when(productoService.newProducto(Mockito.any(ProductoDTO.class))).thenReturn(productoDTO);

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test Producto"));
    }

    @Test
    void testUpdateProducto() throws Exception {
        UpdateProductoDTO updateDTO = new UpdateProductoDTO();
        updateDTO.setNombre("Producto Actualizado");

        Mockito.when(productoService.updateProducto(Mockito.any(UpdateProductoDTO.class), Mockito.eq(1L)))
                .thenReturn(updateDTO);

        mockMvc.perform(put("/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto Actualizado"));
    }

    @Test
    void testGetAllProductos() throws Exception {
        Mockito.when(productoService.getAllProductos()).thenReturn(Collections.singletonList(productoDTO));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Test Producto"));
    }

    @Test
    void testGetProductoById() throws Exception {
        Mockito.when(productoService.getProductoById(1L)).thenReturn(productoDTO);

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test Producto"));
    }

    @Test
    void testDeleteProducto() throws Exception {
        Mockito.doNothing().when(productoService).deleteProducto(1L);

        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActualizarStock() throws Exception {
        // Crear y llenar el DTO de entrada
        StockUpdateDTO stockDTO = new StockUpdateDTO();
        stockDTO.setId(1L);
        stockDTO.setCantidad(5);
        stockDTO.setPrecio(new BigDecimal("150.00"));
    
        // Crear y llenar el DTO de salida simulado desde el service
        UpdateProductoDTO updateProductoDTO = new UpdateProductoDTO();
        updateProductoDTO.setNombre("Teclado mecánico");
        updateProductoDTO.setDescripcion("Teclado mecánico RGB con switches azules");
        updateProductoDTO.setStockActual(15); // Suponiendo que la cantidad anterior era 10 y se sumaron 5
        updateProductoDTO.setStockMinimo(2);
        updateProductoDTO.setPrecio(new BigDecimal("150.00"));
        updateProductoDTO.setDescuentoPromocional(new BigDecimal("10.00"));
        updateProductoDTO.setNombreCategoria("Periféricos");
    
        // Simulación del service
        Mockito.when(productoService.updateProductoStock(1L, stockDTO)).thenReturn(updateProductoDTO);
    
        // Ejecución y validación del endpoint
        mockMvc.perform(put("/productos/1/actualizar_stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(stockDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Teclado mecánico"))
                .andExpect(jsonPath("$.descripcion").value("Teclado mecánico RGB con switches azules"))
                .andExpect(jsonPath("$.stockActual").value(15))
                .andExpect(jsonPath("$.stockMinimo").value(2))
                .andExpect(jsonPath("$.precio").value(150.00))
                .andExpect(jsonPath("$.descuentoPromocional").value(10.00))
                .andExpect(jsonPath("$.nombreCategoria").value("Periféricos"));
    }
    

    @Test
    void testActualizarDescuento() throws Exception {
        BigDecimal nuevoDescuento = new BigDecimal("10.00");
        productoDTO.setDescuentoPromocional(nuevoDescuento);

        Mockito.when(productoService.updateDescuento(1L, nuevoDescuento)).thenReturn(productoDTO);

        mockMvc.perform(put("/productos/1/10.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descuentoPromocional").value(10.00));
    }

    @Test
    void testVerificarStockVenta() throws Exception {
    VerificacionStockVentaDTO dto = new VerificacionStockVentaDTO();
    dto.setId(1L);
    dto.setCantidadRequerida(3);

    Mockito.when(productoService.verificarStockParaVenta(Mockito.anyList())).thenReturn(true);

    mockMvc.perform(post("/productos/verificar_stock")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(Collections.singletonList(dto))))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
    }


    // Utilidad
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
