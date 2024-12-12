package br.com.ipgest.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import br.com.ipgest.model.Identifiable;
import br.com.ipgest.service.BaseService;

public class BasePageControllerTest {

    @Mock
    private BaseService<Identifiable<Long>, Long> service;

    @Mock
    private Model model;

    @InjectMocks
    private final BasePageController<Identifiable<Long>, Long> controller = new BasePageController<Identifiable<Long>, Long>() {
        @Override
        protected Identifiable<Long> createNewEntity() {
            return new Identifiable<Long>() {
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

        @Override
        protected void validateEntity(Long id, Identifiable<Long> entity, Long selectedIgrejaId) {
            // Validation logic here
        }

        @Override
        protected String getEntityFormView() {
            return "entityFormView";
        }

        @Override
        protected String getEntityListView() {
            return "entityListView";
        }
    };

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowPage() {
        List<Identifiable<Long>> entities = Arrays.asList(mock(Identifiable.class), mock(Identifiable.class));
        when(service.findAll()).thenReturn(entities);

        String viewName = controller.showPage(model);

        verify(model).addAttribute("entities", entities);
        verify(model).addAttribute("isEdit", false);
        assertEquals("entityListView", viewName);
    }

    @Test
    public void testNewEntity() {
        String viewName = controller.newEntity(model);

        verify(model).addAttribute(eq("isEdit"), eq(false));
        verify(model).addAttribute(eq("entity"), any(Identifiable.class));
        assertEquals("entityFormView", viewName);
    }

    @Test
    public void testEditEntity() {
        Identifiable<Long> entity = mock(Identifiable.class);
        when(service.findById(1L)).thenReturn(entity);

        String viewName = controller.editEntity(1L, model);

        verify(model).addAttribute("entity", entity);
        verify(model).addAttribute("isEdit", true);
        assertEquals("entityFormView", viewName);
    }

    @Test
    public void testSaveEntity() {
        Identifiable<Long> entity = mock(Identifiable.class);

        String viewName = controller.saveEntity(1L, entity, 1L);

        verify(service).save(entity);
        assertEquals("entityListView", viewName);
    }

    @Test
    public void testDeleteEntity() {
        String viewName = controller.deleteEntity(1L);

        verify(service).deleteById(1L);
        assertEquals("entityListView", viewName);
    }
}