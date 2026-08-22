package com.kanapa4.medical_clinic_proxy.client;

import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.DoctorPageResponse;
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
    void getPatientVisits_WireMockConfigured_ReturnVisitList() {
        stubFor(get(urlEqualTo("/visits/patient/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 1, \"durationInMinutes\": 30}]")));
        List<VisitDto> visits = medicalClinicClient.getPatientVisits(1L);
        assertNotNull(visits);
        assertFalse(visits.isEmpty());
        assertEquals(1, visits.size());
        assertEquals(1L, visits.getFirst().getId());
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
    void getAvailableVisitsByDoctor_WireMockConfigured_ReturnVisitList() {
        stubFor(get(urlEqualTo("/visits/available/doctor/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 5, \"doctorId\": 1}]")));
        List<VisitDto> visits = medicalClinicClient.getAvailableVisitsByDoctor(1L);
        assertNotNull(visits);
        assertFalse(visits.isEmpty());
        assertEquals(5L, visits.getFirst().getId());
    }

    @Test
    void getAvailableVisitsBySpecializationAndDate_WireMockConfigured_ReturnVisitList() {
        stubFor(get(urlEqualTo("/visits/available?specialization=CARDIOLOGIST&date=2026-08-18"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 10}]")));
        List<VisitDto> visits = medicalClinicClient.getAvailableVisitsBySpecializationAndDate(
                Specialization.CARDIOLOGIST, LocalDate.of(2026, 8, 18));
        assertNotNull(visits);
        assertFalse(visits.isEmpty());
        assertEquals(10L, visits.getFirst().getId());
    }

    @Test
    void getDoctorVisits_WireMockConfigured_ReturnVisitList() {
        stubFor(get(urlEqualTo("/visits/doctor/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 15}]")));
        List<VisitDto> visits = medicalClinicClient.getDoctorVisits(1L);
        assertNotNull(visits);
        assertFalse(visits.isEmpty());
        assertEquals(15L, visits.getFirst().getId());
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
    void getAvailableVisitsByDateRange_WireMockConfigured_ReturnVisitList() {
        stubFor(get(urlEqualTo("/visits/available-range?specialization=CARDIOLOGIST&startDate=2026-08-18&endDate=2026-08-25"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 20}]")));
        List<VisitDto> visits = medicalClinicClient.getAvailableVisitsByDateRange(
                Specialization.CARDIOLOGIST, LocalDate.of(2026, 8, 18), LocalDate.of(2026, 8, 25));
        assertNotNull(visits);
        assertFalse(visits.isEmpty());
        assertEquals(20L, visits.getFirst().getId());
    }

    @Test
    void getVisitsBySpecializationAndDateRange_WireMockConfigured_ReturnVisitList() {
        stubFor(get(urlEqualTo("/visits/search?specialization=CARDIOLOGIST&startDate=2026-08-18&endDate=2026-08-25"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 25}]")));
        List<VisitDto> visits = medicalClinicClient.getVisitsBySpecializationAndDateRange(
                Specialization.CARDIOLOGIST, LocalDate.of(2026, 8, 18), LocalDate.of(2026, 8, 25));
        assertNotNull(visits);
        assertFalse(visits.isEmpty());
        assertEquals(25L, visits.getFirst().getId());
    }

    @Test
    void getDoctorsBySpecialization_WireMockConfigured_ReturnDoctorPage() {
        stubFor(get(urlEqualTo("/doctors?specialization=CARDIOLOGIST&page=0&size=10"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"content\":[{\"id\": 1, \"firstName\": \"John\"}], \"totalPages\": 1, \"totalElements\": 1, \"size\": 10, \"number\": 0}")));
        DoctorPageResponse response = medicalClinicClient.getDoctorsBySpecialization(
                Specialization.CARDIOLOGIST, 0, 10);
        assertNotNull(response);
        assertNotNull(response.getContent());
        assertFalse(response.getContent().isEmpty());
        assertEquals(1L, response.getContent().getFirst().getId());
        assertEquals("John", response.getContent().getFirst().getFirstName());
    }
}
