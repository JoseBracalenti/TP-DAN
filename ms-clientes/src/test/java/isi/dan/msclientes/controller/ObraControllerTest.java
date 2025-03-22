package isi.dan.msclientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.msclientes.dto.ClienteDTO;
import isi.dan.msclientes.dto.CreateObraDTO;
import isi.dan.msclientes.dto.ObraDTO;
import isi.dan.msclientes.dto.UpdateObraDTO;
import isi.dan.msclientes.exception.ObraNotFoundException;
import isi.dan.msclientes.servicios.ObraService;

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

@WebMvcTest(ObraController.class)
public class ObraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObraService obraService;

    private ObraDTO obra;

    private CreateObraDTO createObra;

    private UpdateObraDTO updateObra;

    private ClienteDTO cliente;

    @BeforeEach
    void setUp() {
        cliente = Mockito.mock(ClienteDTO.class);
        cliente.setId(1);

        obra = new ObraDTO();
        obra.setId(1);
        obra.setDireccion("Direccion Test Obra");
        obra.setEsRemodelacion(false);
        obra.setLat(1);
        obra.setLng(1);
        obra.setEstado("HABILITADA");
        obra.setCliente(cliente);
        obra.setPresupuesto(BigDecimal.valueOf(100000));

        createObra = new CreateObraDTO();
        createObra.setDireccion("Direccion Test Obra");
        createObra.setEsRemodelacion(false);
        createObra.setLat(1);
        createObra.setLng(1);
        createObra.setClienteId(1);
        createObra.setPresupuesto(BigDecimal.valueOf(100000));

        updateObra = new UpdateObraDTO();
        updateObra.setDireccion("Direccion Actualizada Obra");
        updateObra.setEsRemodelacion(true);
        updateObra.setLat(3);
        updateObra.setLng(3);
        updateObra.setPresupuesto(BigDecimal.valueOf(20000));
    }

    @Test
    void testGetAll() throws Exception {
        Mockito.when(obraService.findAll()).thenReturn(Collections.singletonList(obra));

        mockMvc.perform(get("/api/obras"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].direccion").value("Direccion Test Obra"));
    }

    @Test
    void testGetById() throws Exception {
        Mockito.when(obraService.findById(1)).thenReturn(obra);

        mockMvc.perform(get("/api/obras/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.direccion").value("Direccion Test Obra"));
    }

    @Test
    void testGetById_NotFound() throws Exception {
        Mockito.when(obraService.findById(2)).thenThrow(ObraNotFoundException.class);

        mockMvc.perform(get("/api/obras/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {
        Mockito.when(obraService.save(Mockito.any(CreateObraDTO.class))).thenReturn(obra);

        mockMvc.perform(post("/api/obras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createObra)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccion").value("Direccion Test Obra"));
    }

    @Test
    void testUpdate() throws Exception {
        ObraDTO updatedObra = new ObraDTO();
        updatedObra.setId(1);
        updatedObra.setDireccion("Direccion Actualizada Obra");
        updatedObra.setEsRemodelacion(true);
        updatedObra.setLat(3);
        updatedObra.setLng(3);
        updatedObra.setCliente(cliente);
        updatedObra.setEstado("HABILITADA");
        updatedObra.setPresupuesto(BigDecimal.valueOf(20000));

        Mockito.when(obraService.update(Mockito.any(UpdateObraDTO.class))).thenReturn(updatedObra);

        mockMvc.perform(put("/api/obras/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateObra)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccion").value("Direccion Actualizada Obra"))
                .andExpect(jsonPath("$.esRemodelacion").value(true))
                .andExpect(jsonPath("$.lat").value(3))
                .andExpect(jsonPath("$.lng").value(3))
                .andExpect(jsonPath("$.presupuesto").value(20000));
    }

    @Test
    void testDelete() throws Exception {
        Mockito.when(obraService.findById(1)).thenReturn(obra);
        Mockito.doNothing().when(obraService).deleteById(1);

        mockMvc.perform(delete("/api/obras/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testPendiente() throws Exception {
        Mockito.doNothing().when(obraService).pendienteObra(1);

        mockMvc.perform(put("/api/obras/1/pendiente"))
                .andExpect(status().isOk()); // Expect HTTP 200 OK

        Mockito.verify(obraService, Mockito.times(1)).pendienteObra(1);
    }

    @Test
    void testHabilitar() throws Exception {
        Mockito.doNothing().when(obraService).habilitarObra(1);

        mockMvc.perform(put("/api/obras/1/habilitar"))
                .andExpect(status().isOk()); // Expect HTTP 200 OK

        Mockito.verify(obraService, Mockito.times(1)).habilitarObra(1);
    }

    @Test
    void testFinalizar() throws Exception {
        Mockito.doNothing().when(obraService).finalizarObra(1);

        mockMvc.perform(put("/api/obras/1/finalizar"))
                .andExpect(status().isOk()); // Expect HTTP 200 OK

        Mockito.verify(obraService, Mockito.times(1)).finalizarObra(1);
    }

    @Test
    void testHabilitarError() throws Exception {
        Mockito.doThrow(IllegalArgumentException.class).when(obraService).habilitarObra(1);

        mockMvc.perform(put("/api/obras/1/habilitar"))
                .andExpect(status().isBadRequest());

        Mockito.verify(obraService, Mockito.times(1)).habilitarObra(1);
    }

    @Test
    void testPendienteError() throws Exception {
        Mockito.doThrow(IllegalArgumentException.class).when(obraService).pendienteObra(1);

        mockMvc.perform(put("/api/obras/1/pendiente"))
                .andExpect(status().isBadRequest());

        Mockito.verify(obraService, Mockito.times(1)).pendienteObra(1);
    }

    @Test
    void testFinalizarError() throws Exception {
        Mockito.doThrow(IllegalArgumentException.class).when(obraService).finalizarObra(1);

        mockMvc.perform(put("/api/obras/1/finalizar"))
                .andExpect(status().isBadRequest());

        Mockito.verify(obraService, Mockito.times(1)).finalizarObra(1);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
