package isi.dan.msclientes.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.msclientes.dto.EstadoDeObraDTO;
import isi.dan.msclientes.model.EstadoDeObra;
import isi.dan.msclientes.servicios.EstadoDeObraService;

@WebMvcTest(EstadoDeObraController.class)
public class EstadoDeObraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadoDeObraService estadoDeObraService;

    private EstadoDeObra estadoDeObra;

    @BeforeEach
    void setUp() {
        estadoDeObra = new EstadoDeObra();
        estadoDeObra.setId(1);
        estadoDeObra.setEstado("NUEVO ESTADO");
    }

    @Test
    void testCreate() throws Exception {
        EstadoDeObraDTO dto = new EstadoDeObraDTO();
        dto.setEstado("NUEVO ESTADO");
        Mockito.when(estadoDeObraService.create(dto.getEstado())).thenReturn(estadoDeObra);

        mockMvc.perform(post("/api/estado-de-obra")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("NUEVO ESTADO"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}