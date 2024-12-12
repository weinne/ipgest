package br.com.ipgest.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import br.com.ipgest.model.Identifiable;
import br.com.ipgest.service.BaseService;

@WebMvcTest(BaseController.class)
public class BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BaseService<Identifiable<Long>, Long> service;

    private Identifiable<Long> entity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = new Identifiable<Long>() {
            private Long id;

            @Override
            public Long getId() {
                return id;
            }

            @Override
            public void setId(Long id) {
                this.id = id;
            }
        };
    }

    @Test
    public void testGetAll() throws Exception {
        List<Identifiable<Long>> entities = Arrays.asList(entity);
        when(service.findAll()).thenReturn(entities);

        mockMvc.perform(get("/api/entities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(entity.getId()));
    }

    @Test
    public void testGetById() throws Exception {
        when(service.findById(1L)).thenReturn(entity);

        mockMvc.perform(get("/api/entities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()));
    }

    @Test
    public void testCreate() throws Exception {
        when(service.save(any())).thenReturn(entity);

        mockMvc.perform(post("/api/entities")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()));
    }

    @Test
    public void testUpdate() throws Exception {
        when(service.save(any())).thenReturn(entity);

        mockMvc.perform(put("/api/entities/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()));
    }

    @Test
    public void testDelete() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(delete("/api/entities/1"))
                .andExpect(status().isOk());
    }
}