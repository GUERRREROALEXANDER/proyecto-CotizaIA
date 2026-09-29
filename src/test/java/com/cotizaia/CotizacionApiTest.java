package com.cotizaia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CotizacionApiTest {
    @Autowired MockMvc mvc;
    private static final String BRIEF = """
        {"cliente":"Restaurante La Mesa", "texto":"Necesito una página para mi restaurante con menú, reservas y pagos para diciembre", "tarifa":50000.00}
        """;
    private static final String DECISION = """
        {"revisor":"Revisor de prueba", "motivo":"Revisión humana del alcance ilustrativo"}
        """;

    @Test void crearConsultarYListar() throws Exception {
        String ubicacion = crear();
        mvc.perform(get(ubicacion)).andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente").value("Restaurante La Mesa"))
                .andExpect(jsonPath("$.textoOriginal").value("Necesito una página para mi restaurante con menú, reservas y pagos para diciembre"))
                .andExpect(jsonPath("$.requisitos", hasSize(4)))
                .andExpect(jsonPath("$.preguntasAclaratorias", hasSize(4)))
                .andExpect(jsonPath("$.desgloseHoras", hasSize(6)))
                .andExpect(jsonPath("$.horasTotales").value(40))
                .andExpect(jsonPath("$.tarifa").value(50000))
                .andExpect(jsonPath("$.precioTotal").value(2000000))
                .andExpect(jsonPath("$.moneda").value("COP"))
                .andExpect(jsonPath("$.estado").value("EN_REVISION"))
                .andExpect(jsonPath("$.fecha").isNotEmpty())
                .andExpect(jsonPath("$.historial", hasSize(0)));
        mvc.perform(get("/api/propuestas")).andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"aprobar", "rechazar"})
    void transicionFinalYBloqueoDeAmbasAcciones(String accion) throws Exception {
        String ubicacion = crear();
        String estado = accion.equals("aprobar") ? "APROBADA" : "RECHAZADA";
        mvc.perform(post(ubicacion + "/" + accion).contentType("application/json").content(DECISION))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value(estado))
                .andExpect(jsonPath("$.historial", hasSize(1)))
                .andExpect(jsonPath("$.historial[0].anterior").value("EN_REVISION"))
                .andExpect(jsonPath("$.historial[0].nuevo").value(estado))
                .andExpect(jsonPath("$.historial[0].revisor").value("Revisor de prueba"));
        for (String intento : new String[]{"aprobar", "rechazar"}) {
            mvc.perform(post(ubicacion + "/" + intento).contentType("application/json").content(DECISION))
                    .andExpect(status().isConflict()).andExpect(jsonPath("$.status").value(409));
        }
        mvc.perform(get(ubicacion)).andExpect(jsonPath("$.estado").value(estado))
                .andExpect(jsonPath("$.historial", hasSize(1)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{}", "null", "{", 
        "{\"cliente\":\"X\",\"texto\":\"          \",\"tarifa\":10}",
        "{\"cliente\":\"X\",\"texto\":\"Necesito una web\",\"tarifa\":-1}",
        "{\"cliente\":\"X\",\"texto\":\"Necesito una web\",\"tarifa\":0}",
        "{\"cliente\":\"X\",\"texto\":\"Necesito una web\",\"tarifa\":1.001}",
        "{\"cliente\":\"X\",\"texto\":\"Necesito una web\",\"tarifa\":1000000000}"
    })
    void rechazaBriefInvalido(String json) throws Exception {
        mvc.perform(post("/api/briefs").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
    }

    @Test void erroresDeConsultaYDecision() throws Exception {
        mvc.perform(get("/api/propuestas/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
        mvc.perform(get("/api/propuestas/no-es-uuid")).andExpect(status().isBadRequest());
        mvc.perform(post(crear() + "/rechazar").contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest());
    }

    private String crear() throws Exception {
        MvcResult r = mvc.perform(post("/api/briefs").contentType("application/json").content(BRIEF))
                .andExpect(status().isCreated()).andExpect(header().exists("Location")).andReturn();
        return r.getResponse().getHeader("Location");
    }
}
