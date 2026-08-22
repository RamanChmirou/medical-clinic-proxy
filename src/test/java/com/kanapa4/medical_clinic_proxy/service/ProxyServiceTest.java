package com.kanapa4.medical_clinic_proxy.service;

import com.kanapa4.medical_clinic_proxy.client.MedicalClinicClient;
import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.DoctorPageResponse;
import com.kanapa4.medical_clinic_proxy.model.DoctorResponse;
import com.kanapa4.medical_clinic_proxy.model.PageResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import com.kanapa4.medical_clinic_proxy.model.VisitResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProxyServiceTest {
    @Mock
    private MedicalClinicClient client;
    @InjectMocks
    private ProxyService proxyService;

    @Test
    void getPatientVisits_Success_ReturnVisits() {
        Long patientId = 1L;
        VisitDto dto = new VisitDto();
        dto.setId(10L);
        List<VisitDto> expected = List.of(dto);
        when(client.getPatientVisits(patientId)).thenReturn(expected);
        List<VisitResponse> result = proxyService.getPatientVisits(patientId);
        assertEquals(1, result.size());
        assertEquals(10L, result.getFirst().getId());
        verify(client).getPatientVisits(patientId);
    }

    @Test
    void bookVisit_Success_ReturnBookedVisit() {
        Long visitId = 1L;
        Long patientId = 2L;
        VisitDto dto = new VisitDto();
        dto.setId(10L);
        when(client.bookVisit(visitId, patientId)).thenReturn(dto);
        VisitResponse result = proxyService.bookVisit(visitId, patientId);
        assertEquals(10L, result.getId());
        verify(client).bookVisit(visitId, patientId);
    }

    @Test
    void getAvailableVisitsByDoctor_Success_ReturnVisits() {
        Long doctorId = 1L;
        VisitDto dto = new VisitDto();
        dto.setId(15L);
        when(client.getAvailableVisitsByDoctor(doctorId)).thenReturn(List.of(dto));
        List<VisitResponse> result = proxyService.getAvailableVisitsByDoctor(doctorId);
        assertEquals(1, result.size());
        assertEquals(15L, result.getFirst().getId());
        verify(client).getAvailableVisitsByDoctor(doctorId);
    }

    @Test
    void getAvailableVisitsBySpecializationAndDate_Success_ReturnVisits() {
        LocalDate date = LocalDate.of(2026, 8, 18);
        VisitDto dto = new VisitDto();
        dto.setId(20L);
        when(client.getAvailableVisitsBySpecializationAndDate(Specialization.CARDIOLOGIST, date)).thenReturn(List.of(dto));
        List<VisitResponse> result = proxyService.getAvailableVisitsBySpecializationAndDate(Specialization.CARDIOLOGIST, date);
        assertEquals(1, result.size());
        assertEquals(20L, result.getFirst().getId());
        verify(client).getAvailableVisitsBySpecializationAndDate(Specialization.CARDIOLOGIST, date);
    }

    @Test
    void getDoctorVisits_Success_ReturnVisits() {
        Long doctorId = 1L;
        VisitDto dto = new VisitDto();
        dto.setId(25L);
        when(client.getDoctorVisits(doctorId)).thenReturn(List.of(dto));
        List<VisitResponse> result = proxyService.getDoctorVisits(doctorId);
        assertEquals(1, result.size());
        assertEquals(25L, result.getFirst().getId());
        verify(client).getDoctorVisits(doctorId);
    }

    @Test
    void cancelVisit_Success_ReturnCancelledVisit() {
        Long visitId = 1L;
        VisitDto dto = new VisitDto();
        dto.setId(1L);
        when(client.cancelVisit(visitId)).thenReturn(dto);
        VisitResponse result = proxyService.cancelVisit(visitId);
        assertEquals(1L, result.getId());
        verify(client).cancelVisit(visitId);
    }

    @Test
    void getAvailableVisitsByDateRange_Success_ReturnVisits() {
        LocalDate start = LocalDate.of(2026, 8, 18);
        LocalDate end = LocalDate.of(2026, 8, 25);
        VisitDto dto = new VisitDto();
        dto.setId(30L);
        when(client.getAvailableVisitsByDateRange(Specialization.CARDIOLOGIST, start, end)).thenReturn(List.of(dto));
        List<VisitResponse> result = proxyService.getAvailableVisitsByDateRange(Specialization.CARDIOLOGIST, start, end);
        assertEquals(1, result.size());
        assertEquals(30L, result.getFirst().getId());
        verify(client).getAvailableVisitsByDateRange(Specialization.CARDIOLOGIST, start, end);
    }

    @Test
    void getVisitsBySpecializationAndDateRange_Success_ReturnVisits() {
        LocalDate start = LocalDate.of(2026, 8, 18);
        LocalDate end = LocalDate.of(2026, 8, 25);
        VisitDto dto = new VisitDto();
        dto.setId(35L);
        when(client.getVisitsBySpecializationAndDateRange(Specialization.CARDIOLOGIST, start, end)).thenReturn(List.of(dto));
        List<VisitResponse> result = proxyService.getVisitsBySpecializationAndDateRange(Specialization.CARDIOLOGIST, start, end);
        assertEquals(1, result.size());
        assertEquals(35L, result.getFirst().getId());
        verify(client).getVisitsBySpecializationAndDateRange(Specialization.CARDIOLOGIST, start, end);
    }

    @Test
    void getDoctorsBySpecialization_Success_ReturnDoctors() {
        DoctorDto dto = new DoctorDto();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setSpecialization(Specialization.CARDIOLOGIST);
        DoctorPageResponse pageResponse = new DoctorPageResponse(List.of(dto), 1, 1L, 10, 0);
        when(client.getDoctorsBySpecialization(Specialization.CARDIOLOGIST, 0, 10)).thenReturn(pageResponse);
        PageResponse<DoctorResponse> result = proxyService.getDoctorsBySpecialization(Specialization.CARDIOLOGIST, 0, 10);
        assertEquals(1, result.content().size());
        assertEquals(1L, result.content().getFirst().getId());
        assertEquals("John", result.content().getFirst().getFirstName());
        assertEquals("Doe", result.content().getFirst().getLastName());
        assertEquals(Specialization.CARDIOLOGIST, result.content().getFirst().getSpecialization());
        assertEquals(0, result.page());
        assertEquals(10, result.size());
        assertEquals(1L, result.totalElements());
        assertEquals(1, result.totalPages());
        verify(client).getDoctorsBySpecialization(Specialization.CARDIOLOGIST, 0, 10);
    }
}
