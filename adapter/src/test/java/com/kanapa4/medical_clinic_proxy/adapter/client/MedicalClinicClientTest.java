package com.kanapa4.medical_clinic_proxy.adapter.client;

import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.PageResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock
public class MedicalClinicClientTest {

    @Autowired
    private MedicalClinicClient medicalClinicClient;

    @Test
    void getVisits_WireMockConfigured_ReturnVisitPage() {
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("patientId", equalTo("1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"content\":[{\"id\":1,\"durationInMinutes\":30}],\"totalPages\":1,\"totalElements\":1,\"size\":20,\"number\":0}")));
        PageResponse<VisitDto> page = medicalClinicClient.getVisits(1L, null, null, null, null, null);
        assertNotNull(page);
        assertFalse(page.content().isEmpty());
        assertEquals(1L, page.content().getFirst().getId());
    }

    @Test
    void bookVisit_WireMockConfigured_ReturnBookedVisit() {
        stubFor(patch(urlEqualTo("/visits/1/book/2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"patientId\": 2}")));
        VisitDto visit = medicalClinicClient.bookVisit(1L, 2L);
        assertNotNull(visit);
        assertEquals(1L, visit.getId());
        assertEquals(2L, visit.getPatientId());
    }

    @Test
    void getVisits_WithSpecializationAndDate_ReturnVisitPage() {
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("specialization", equalTo("CARDIOLOGY"))
                .withQueryParam("startDate", equalTo("2026-08-18"))
                .withQueryParam("available", equalTo("true"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"content\":[{\"id\":10}],\"totalPages\":1,\"totalElements\":1,\"size\":20,\"number\":0}")));
        PageResponse<VisitDto> page = medicalClinicClient.getVisits(null, null,
                Specialization.CARDIOLOGY, LocalDate.of(2026, 8, 18), null, true);
        assertNotNull(page);
        assertFalse(page.content().isEmpty());
        assertEquals(10L, page.content().getFirst().getId());
    }

    @Test
    void cancelVisit_WireMockConfigured_ReturnCancelledVisit() {
        stubFor(patch(urlEqualTo("/visits/1/cancel"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1}")));
        VisitDto visit = medicalClinicClient.cancelVisit(1L);
        assertNotNull(visit);
        assertEquals(1L, visit.getId());
    }

    @Test
    void getDoctorsBySpecialization_WireMockConfigured_ReturnDoctorList() {
        stubFor(get(urlPathEqualTo("/doctors"))
                .withQueryParam("specialization", equalTo("CARDIOLOGY"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"content\":[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}],\"page\":0,\"size\":10,\"totalElements\":1,\"totalPages\":1}")));

        PageResponse<DoctorDto> page = medicalClinicClient.getDoctorsBySpecialization(Specialization.CARDIOLOGY);

        assertNotNull(page);
        assertFalse(page.content().isEmpty());
        assertEquals(1L, page.content().getFirst().getId());
        assertEquals("John", page.content().getFirst().getFirstName());
    }
}
