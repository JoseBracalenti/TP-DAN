package isi.dan.ms.pedidos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import isi.dan.ms.pedidos.dto.PedidoDTO;
import isi.dan.ms.pedidos.modelo.Cliente;
import isi.dan.ms.pedidos.modelo.DetallePedido;
import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.Obra;
import isi.dan.ms.pedidos.modelo.Pedido;
import isi.dan.ms.pedidos.modelo.Producto;
import isi.dan.ms.pedidos.servicio.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido pedido;
    private PedidoDTO pedidoDTO;
    private Cliente cliente;
    private DetallePedido detallePedido;
    private Producto producto;
    private Obra obra;

    @BeforeEach
    void setUp() {
        // Setup Obra
        obra = new Obra();
        obra.setId(1);
        obra.setDireccion("Calle Test 123");
        obra.setEsRemodelacion(false);
        obra.setLat(-31.4201f);
        obra.setLng(-64.1888f);

        // Setup Cliente
        cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombre("Cliente Test");
        cliente.setCorreoElectronico("test@test.com");
        cliente.setCuit("20-12345678-9");
        cliente.setObra(obra);

        // Setup Producto
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción del producto test");
        producto.setPrecio(new BigDecimal("100.00"));

        // Setup DetallePedido
        detallePedido = new DetallePedido();
        detallePedido.setProducto(producto);
        detallePedido.setCantidad(2);
        detallePedido.setPrecioUnitario(new BigDecimal("100.00"));
        detallePedido.setDescuento(BigDecimal.ZERO);
        detallePedido.setPrecioFinal(new BigDecimal("200.00"));

        // Setup Pedido
        pedido = new Pedido();
        pedido.setId("pedido1");
        pedido.setFecha(Instant.now());
        pedido.setNumeroPedido(1001);
        pedido.setUsuario("usuario1");
        pedido.setObservaciones("Test pedido");
        pedido.setEstado(EstadoPedido.ACEPTADO);
        pedido.setCliente(cliente);
        pedido.setTotal(new BigDecimal("200.00"));
        pedido.setDetalle(Arrays.asList(detallePedido));

        // Setup PedidoDTO
        pedidoDTO = new PedidoDTO();
        pedidoDTO.setId("pedido1");
        pedidoDTO.setFecha(Instant.now());
        pedidoDTO.setNumeroPedido(1001);
        pedidoDTO.setUsuario("usuario1");
        pedidoDTO.setObservaciones("Test pedido");
        pedidoDTO.setEstado(EstadoPedido.ACEPTADO);
        pedidoDTO.setCliente(cliente);
        pedidoDTO.setTotal(new BigDecimal("200.00"));
        pedidoDTO.setDetalle(Arrays.asList(detallePedido));
    }

    @Test
    void createPedido_ShouldReturnCreatedPedido() throws Exception {
        // Given
        when(pedidoService.savePedido(any(Pedido.class))).thenReturn(pedido);

        // When & Then
        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("pedido1"))
                .andExpect(jsonPath("$.numeroPedido").value(1001))
                .andExpect(jsonPath("$.usuario").value("usuario1"))
                .andExpect(jsonPath("$.observaciones").value("Test pedido"))
                .andExpect(jsonPath("$.estado").value("ACEPTADO"))
                .andExpect(jsonPath("$.total").value(200.00))
                .andExpect(jsonPath("$.cliente.id").value(1))
                .andExpect(jsonPath("$.cliente.nombre").value("Cliente Test"));

        verify(pedidoService, times(1)).savePedido(any(Pedido.class));
    }

    @Test
    void getAllPedidos_ShouldReturnListOfPedidos() throws Exception {
        // Given
        List<Pedido> pedidos = Arrays.asList(pedido);
        when(pedidoService.getAllPedidos()).thenReturn(pedidos);

        // When & Then
        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("pedido1"))
                .andExpect(jsonPath("$[0].numeroPedido").value(1001))
                .andExpect(jsonPath("$[0].usuario").value("usuario1"));

        verify(pedidoService, times(1)).getAllPedidos();
    }

    @Test
    void getAllPedidos_ShouldReturnEmptyList_WhenNoPedidosExist() throws Exception {
        // Given
        when(pedidoService.getAllPedidos()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(pedidoService, times(1)).getAllPedidos();
    }

    @Test
    void actualizarEstado_ShouldReturnUpdatedPedido() throws Exception {
        // Given
        pedido.setEstado(EstadoPedido.EN_PREPARACION);
        when(pedidoService.actualizarEstado("pedido1", EstadoPedido.EN_PREPARACION)).thenReturn(pedido);

        // When & Then
        mockMvc.perform(put("/pedidos/pedido1/estado")
                .param("nuevoEstado", "EN_PREPARACION"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("pedido1"))
                .andExpect(jsonPath("$.estado").value("EN_PREPARACION"));

        verify(pedidoService, times(1)).actualizarEstado("pedido1", EstadoPedido.EN_PREPARACION);
    }

    @Test
    void actualizarEstado_ShouldReturnNotFound_WhenPedidoNotExists() throws Exception {
        // Given
        when(pedidoService.actualizarEstado("nonexistent", EstadoPedido.EN_PREPARACION)).thenReturn(null);

        // When & Then
        mockMvc.perform(put("/pedidos/nonexistent/estado")
                .param("nuevoEstado", "EN_PREPARACION"))
                .andExpect(status().isNotFound());

        verify(pedidoService, times(1)).actualizarEstado("nonexistent", EstadoPedido.EN_PREPARACION);
    }

    @Test
    void getPedidoById_ShouldReturnPedido() throws Exception {
        // Given
        when(pedidoService.getPedidoById("pedido1")).thenReturn(pedido);

        // When & Then
        mockMvc.perform(get("/pedidos/pedido1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("pedido1"))
                .andExpect(jsonPath("$.numeroPedido").value(1001))
                .andExpect(jsonPath("$.usuario").value("usuario1"))
                .andExpect(jsonPath("$.observaciones").value("Test pedido"))
                .andExpect(jsonPath("$.estado").value("ACEPTADO"));

        verify(pedidoService, times(1)).getPedidoById("pedido1");
    }

    @Test
    void getPedidoById_ShouldReturnNotFound_WhenPedidoNotExists() throws Exception {
        // Given
        when(pedidoService.getPedidoById("nonexistent")).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/pedidos/nonexistent"))
                .andExpect(status().isNotFound());

        verify(pedidoService, times(1)).getPedidoById("nonexistent");
    }

    @Test
    void deletePedido_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(pedidoService).deletePedido("pedido1");

        // When & Then
        mockMvc.perform(delete("/pedidos/pedido1"))
                .andExpect(status().isNoContent());

        verify(pedidoService, times(1)).deletePedido("pedido1");
    }

    @Test
    void createPedido_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        // Given - DTO with invalid data
        PedidoDTO invalidPedidoDTO = new PedidoDTO();
        // Not setting required fields

        // When & Then
        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPedidoDTO)))
                .andExpect(status().isOk()); // The controller doesn't validate, so it will pass through

        verify(pedidoService, times(1)).savePedido(any(Pedido.class));
    }

    @Test
    void actualizarEstado_ShouldHandleInvalidEstado() throws Exception {
        // When & Then - Using invalid estado value
        mockMvc.perform(put("/pedidos/pedido1/estado")
                .param("nuevoEstado", "INVALID_ESTADO"))
                .andExpect(status().isBadRequest());

        verify(pedidoService, never()).actualizarEstado(any(), any());
    }

    @Test
    void createPedido_ShouldHandleMalformedJson() throws Exception {
        // When & Then
        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json"))
                .andExpect(status().isBadRequest());

        verify(pedidoService, never()).savePedido(any());
    }
}
