package com.kanapa4.medical_clinic_proxy.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanapa4.medical_clinic_proxy.domain.ProxyService;
import com.kanapa4.medical_clinic_proxy.model.BookVisitRequest;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.pub.DoctorResponse;
import com.kanapa4.medical_clinic_proxy.pub.VisitResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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
    void getVisits_ValidPatientId_Return200() throws Exception {
        when(proxyService.getVisits(1L, null, null, null, null, false)).thenReturn(List.of(new VisitResponse()));
        mockMvc.perform(get("/proxy/visits")
                .param("patientId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getVisits_WithSpecializationAndDate_Return200() throws Exception {
        LocalDate date = LocalDate.of(2026, 8, 18);
        when(proxyService.getVisits(null, null, Specialization.CARDIOLOGY, date, date, false))
                .thenReturn(List.of(new VisitResponse()));
        mockMvc.perform(get("/proxy/visits")
                .param("specialization", "CARDIOLOGY")
                .param("startDate", "2026-08-18")
                .param("endDate", "2026-08-18"))
                .andExpect(status().isOk());
    }

    @Test
    void getVisits_AvailableOnly_Return200() throws Exception {
        when(proxyService.getVisits(null, 1L, null, null, null, true))
                .thenReturn(List.of(new VisitResponse()));
        mockMvc.perform(get("/proxy/visits")
                .param("doctorId", "1")
                .param("availableOnly", "true"))
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

    @Test
    void cancelVisit_ValidVisitId_Return200() throws Exception {
        when(proxyService.cancelVisit(1L)).thenReturn(new VisitResponse());
        mockMvc.perform(patch("/proxy/visits/1/cancel"))
                .andExpect(status().isOk());
    }

    @Test
    void getDoctorsBySpecialization_ValidParams_Return200() throws Exception {
        when(proxyService.getDoctorsBySpecialization(Specialization.CARDIOLOGY))
                .thenReturn(List.of(new DoctorResponse()));
        mockMvc.perform(get("/proxy/doctors")
                .param("specialization", "CARDIOLOGY"))
                .andExpect(status().isOk());
    }
}
