package com.kanapa4.medical_clinic_proxy.controller;

import tools.jackson.databind.ObjectMapper;
import com.kanapa4.medical_clinic_proxy.model.BookVisitRequest;
import com.kanapa4.medical_clinic_proxy.model.DoctorResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitResponse;
import com.kanapa4.medical_clinic_proxy.service.ProxyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
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

    @Test
    void getAvailableVisitsByDoctor_ValidDoctorId_Return200() throws Exception {
        when(proxyService.getAvailableVisitsByDoctor(1L)).thenReturn(List.of(new VisitResponse()));
        mockMvc.perform(get("/proxy/visits/available/doctor/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAvailableVisitsBySpecializationAndDate_ValidParams_Return200() throws Exception {
        LocalDate date = LocalDate.of(2026, 8, 18);
        when(proxyService.getAvailableVisitsBySpecializationAndDate(Specialization.CARDIOLOGY, date))
                .thenReturn(List.of(new VisitResponse()));
        mockMvc.perform(get("/proxy/visits/available")
                .param("specialization", "CARDIOLOGY")
                .param("date", "2026-08-18"))
                .andExpect(status().isOk());
    }

    @Test
    void getDoctorVisits_ValidDoctorId_Return200() throws Exception {
        when(proxyService.getDoctorVisits(1L)).thenReturn(List.of(new VisitResponse()));
        mockMvc.perform(get("/proxy/doctors/1/visits"))
                .andExpect(status().isOk());
    }

    @Test
    void cancelVisit_ValidVisitId_Return200() throws Exception {
        when(proxyService.cancelVisit(1L)).thenReturn(new VisitResponse());
        mockMvc.perform(patch("/proxy/visits/1/cancel"))
                .andExpect(status().isOk());
    }

    @Test
    void getAvailableVisitsByDateRange_ValidParams_Return200() throws Exception {
        LocalDate start = LocalDate.of(2026, 8, 18);
        LocalDate end = LocalDate.of(2026, 8, 25);
        when(proxyService.getAvailableVisitsByDateRange(Specialization.CARDIOLOGY, start, end))
                .thenReturn(List.of(new VisitResponse()));
        mockMvc.perform(get("/proxy/visits/available-range")
                .param("specialization", "CARDIOLOGY")
                .param("startDate", "2026-08-18")
                .param("endDate", "2026-08-25"))
                .andExpect(status().isOk());
    }

    @Test
    void getVisitsBySpecializationAndDateRange_ValidParams_Return200() throws Exception {
        LocalDate start = LocalDate.of(2026, 8, 18);
        LocalDate end = LocalDate.of(2026, 8, 25);
        when(proxyService.getVisitsBySpecializationAndDateRange(Specialization.CARDIOLOGY, start, end))
                .thenReturn(List.of(new VisitResponse()));
        mockMvc.perform(get("/proxy/visits/search")
                .param("specialization", "CARDIOLOGY")
                .param("startDate", "2026-08-18")
                .param("endDate", "2026-08-25"))
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

