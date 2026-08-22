package com.kanapa4.medical_clinic_proxy.controller;

import tools.jackson.databind.ObjectMapper;
import com.kanapa4.medical_clinic_proxy.model.BookVisitRequest;
import com.kanapa4.medical_clinic_proxy.model.VisitResponse;
import com.kanapa4.medical_clinic_proxy.service.ProxyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProxyController.class)
public class ProxyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProxyService proxyService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getPatientVisits_ValidPatientId_Return200() throws Exception {
        when(proxyService.getPatientVisits(1L)).thenReturn(List.of(new VisitResponse()));
        mockMvc.perform(get("/proxy/patients/1/visits"))
                .andExpect(status().isOk());
    }

    @Test
    void bookVisit_ValidRequest_Return200() throws Exception {
        BookVisitRequest request = new BookVisitRequest(2L);
        when(proxyService.bookVisit(1L, 2L)).thenReturn(new VisitResponse());
        mockMvc.perform(patch("/proxy/visits/1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
