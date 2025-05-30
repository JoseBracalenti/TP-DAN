package isi.dan.msclientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.msclientes.dto.ClienteDTO;
import isi.dan.msclientes.dto.CreateClienteDTO;
import isi.dan.msclientes.exception.ClienteNotFoundException;
import isi.dan.msclientes.servicios.ClienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    private ClienteDTO cliente;

    private CreateClienteDTO createCliente;

    @BeforeEach
    void setUp() {
        cliente = new ClienteDTO();
        cliente.setId(1);
        cliente.setNombre("Test Cliente");
        cliente.setCorreoElectronico("test@cliente.com");
        cliente.setCuit("12998887776");
    }

    @Test
    void testGetAll() throws Exception {
        Mockito.when(clienteService.findAll()).thenReturn(Collections.singletonList(cliente));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Test Cliente"));
    }

    @Test
    void testGetById() throws Exception {
        Mockito.when(clienteService.findById(1)).thenReturn(cliente);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Test Cliente"))
                .andExpect(jsonPath("$.cuit").value("12998887776"));
    }

    @Test
    void testGetById_NotFound() throws Exception {
        Mockito.when(clienteService.findById(2)).thenThrow(ClienteNotFoundException.class);

        mockMvc.perform(get("/api/clientes/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {
        // Create a mock of the ClienteService.save method. When it's called, it returns
        // a DTO since it's expected
        Mockito.when(clienteService.save(Mockito.any(CreateClienteDTO.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createCliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test Cliente"));
    }

    @Test
    void testUpdate() throws Exception {
        Mockito.when(clienteService.findById(1)).thenReturn(cliente);

        ClienteDTO updatedCliente = new ClienteDTO();
        updatedCliente.setId(1);
        updatedCliente.setNombre("Updated Cliente");
        updatedCliente.setCorreoElectronico("updated@cliente.com");
        updatedCliente.setCuit("12998887776");

        Mockito.when(clienteService.update(1, Mockito.any(ClienteDTO.class))).thenReturn(updatedCliente);

        mockMvc.perform(put("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedCliente)))
                .andExpect(jsonPath("$.nombre").value("Updated Cliente"))
                .andExpect(jsonPath("$.correoElectronico").value("updated@cliente.com"));
    }

    @Test
    void testDelete() throws Exception {
        Mockito.when(clienteService.findById(1)).thenReturn(cliente);
        Mockito.doNothing().when(clienteService).deleteById(1);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
