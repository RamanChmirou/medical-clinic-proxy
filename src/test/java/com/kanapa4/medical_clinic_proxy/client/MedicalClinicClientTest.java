package com.kanapa4.medical_clinic_proxy.client;

import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.PageResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;
import java.time.LocalDate;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock
public class MedicalClinicClientTest {
    @Autowired
    private MedicalClinicClient medicalClinicClient;

    @Test
    void getPatientVisits_WireMockConfigured_ReturnVisitPage() {
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("patientId", equalTo("1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"content\":[{\"id\":1,\"durationInMinutes\":30}],\"totalPages\":1,\"totalElements\":1,\"size\":20,\"number\":0}")));
        PageResponse<VisitDto> page = medicalClinicClient.getPatientVisits(1L);
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
    void getAvailableVisitsByDoctor_WireMockConfigured_ReturnVisitPage() {
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("doctorId", equalTo("1"))
                .withQueryParam("available", equalTo("true"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"content\":[{\"id\":5,\"doctorId\":1}],\"totalPages\":1,\"totalElements\":1,\"size\":20,\"number\":0}")));
        PageResponse<VisitDto> page = medicalClinicClient.getAvailableVisitsByDoctor(1L, true);
        assertNotNull(page);
        assertFalse(page.content().isEmpty());
        assertEquals(5L, page.content().getFirst().getId());
    }

    @Test
    void getAvailableVisitsBySpecializationAndDate_WireMockConfigured_ReturnVisitPage() {
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("specialization", equalTo("CARDIOLOGY"))
                .withQueryParam("date", equalTo("2026-08-18"))
                .withQueryParam("available", equalTo("true"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"content\":[{\"id\":10}],\"totalPages\":1,\"totalElements\":1,\"size\":20,\"number\":0}")));
        PageResponse<VisitDto> page = medicalClinicClient.getAvailableVisitsBySpecializationAndDate(
                Specialization.CARDIOLOGY, LocalDate.of(2026, 8, 18), true);
        assertNotNull(page);
        assertFalse(page.content().isEmpty());
        assertEquals(10L, page.content().getFirst().getId());
    }

    @Test
    void getDoctorVisits_WireMockConfigured_ReturnVisitPage() {
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("doctorId", equalTo("1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"content\":[{\"id\":15}],\"totalPages\":1,\"totalElements\":1,\"size\":20,\"number\":0}")));
        PageResponse<VisitDto> page = medicalClinicClient.getDoctorVisits(1L);
        assertNotNull(page);
        assertFalse(page.content().isEmpty());
        assertEquals(15L, page.content().getFirst().getId());
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
    void getAvailableVisitsByDateRange_WireMockConfigured_ReturnVisitPage() {
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("specialization", equalTo("CARDIOLOGY"))
                .withQueryParam("startDate", equalTo("2026-08-18"))
                .withQueryParam("endDate", equalTo("2026-08-25"))
                .withQueryParam("available", equalTo("true"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"content\":[{\"id\":20}],\"totalPages\":1,\"totalElements\":1,\"size\":20,\"number\":0}")));
        PageResponse<VisitDto> page = medicalClinicClient.getAvailableVisitsByDateRange(
                Specialization.CARDIOLOGY, LocalDate.of(2026, 8, 18), LocalDate.of(2026, 8, 25), true);
        assertNotNull(page);
        assertFalse(page.content().isEmpty());
        assertEquals(20L, page.content().getFirst().getId());
    }

    @Test
    void getVisitsBySpecializationAndDateRange_WireMockConfigured_ReturnVisitPage() {
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("specialization", equalTo("CARDIOLOGY"))
                .withQueryParam("startDate", equalTo("2026-08-18"))
                .withQueryParam("endDate", equalTo("2026-08-25"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"content\":[{\"id\":25}],\"totalPages\":1,\"totalElements\":1,\"size\":20,\"number\":0}")));
        PageResponse<VisitDto> page = medicalClinicClient.getVisitsBySpecializationAndDateRange(
                Specialization.CARDIOLOGY, LocalDate.of(2026, 8, 18), LocalDate.of(2026, 8, 25));
        assertNotNull(page);
        assertFalse(page.content().isEmpty());
        assertEquals(25L, page.content().getFirst().getId());
    }

    @Test
    void getDoctorsBySpecialization_WireMockConfigured_ReturnDoctorList() {
        stubFor(get(urlEqualTo("/doctors/specialization/CARDIOLOGY"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}]")));
        List<DoctorDto> doctors = medicalClinicClient.getDoctorsBySpecialization(Specialization.CARDIOLOGY);
        assertNotNull(doctors);
        assertFalse(doctors.isEmpty());
        assertEquals(1L, doctors.getFirst().getId());
        assertEquals("John", doctors.getFirst().getFirstName());
    }
}

