package isi.dan.msclientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

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

    @BeforeEach
    void setUp() {
        obra = new ObraDTO();
        obra.setId(1);
        obra.setDireccion("Direccion Test Obra");
        obra.setPresupuesto(BigDecimal.valueOf(100));
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
                .content(asJsonString(obra)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccion").value("Direccion Test Obra"));
    }

    @Test
    void testUpdate() throws Exception {
        Mockito.when(obraService.findById(1)).thenReturn(obra);
        Mockito.when(obraService.update(Mockito.any(UpdateObraDTO.class))).thenReturn(obra);

        mockMvc.perform(put("/api/obras/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(obra)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccion").value("Direccion Test Obra"));
    }

    @Test
    void testDelete() throws Exception {
        Mockito.when(obraService.findById(1)).thenReturn(obra);
        Mockito.doNothing().when(obraService).deleteById(1);

        mockMvc.perform(delete("/api/obras/1"))
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

