package isi.dan.ms_productos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import isi.dan.ms_productos.dto.CategoriaDTO;
import isi.dan.ms_productos.dto.UpdateCategoriaDTO;
import isi.dan.ms_productos.exception.CategoriaNotFoundException;
import isi.dan.ms_productos.servicio.CategoriaService;
import isi.dan.ms_productos.servicio.EchoClientFeign;

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

@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private EchoClientFeign echoSvc;

    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNombre("Electrónica");
    }

    @Test
    void testCreateCategoriaByName() throws Exception {
        Mockito.when(categoriaService.newCategoria(Mockito.any(CategoriaDTO.class)))
                .thenReturn(categoriaDTO);

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(categoriaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Electrónica"));
    }

    @Test
    void testUpdateCategoria() throws Exception {
        UpdateCategoriaDTO updateDTO = new UpdateCategoriaDTO();
        updateDTO.setNombre("Electrodomésticos");

        Mockito.when(categoriaService.updateCategoria(Mockito.any(UpdateCategoriaDTO.class), Mockito.eq("Electrónica")))
                .thenReturn(updateDTO);

        mockMvc.perform(put("/categorias/Electrónica")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Electrodomésticos"));
    }

    @Test
    void testGetAllCategorias() throws Exception {
        Mockito.when(categoriaService.getAllCategorias())
                .thenReturn(Collections.singletonList(categoriaDTO));

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Electrónica"));
    }

    @Test
    void testGetCategoriaByName() throws Exception {
        Mockito.when(categoriaService.getCategoriaByName("Electrónica"))
                .thenReturn(categoriaDTO);

        mockMvc.perform(get("/categorias/Electrónica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Electrónica"));
    }

    @Test
    void testGetCategoriaByName_NotFound() throws Exception {
        Mockito.when(categoriaService.getCategoriaByName("Inexistente"))
                .thenThrow(new CategoriaNotFoundException("Categoría no encontrada"));

        mockMvc.perform(get("/categorias/Inexistente"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCategoria() throws Exception {
        Mockito.doNothing().when(categoriaService).deleteCategoria("Electrónica");

        mockMvc.perform(delete("/categorias/Electrónica"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetEcho() throws Exception {
        Mockito.when(echoSvc.echo()).thenReturn("echo response");

        mockMvc.perform(get("/categorias/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("echo response"));
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
