package com.kanapa4.medical_clinic_proxy.service;

import com.kanapa4.medical_clinic_proxy.client.MedicalClinicClient;
import com.kanapa4.medical_clinic_proxy.exception.ServiceUnavailableException;
import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProxyServiceTest {
    @Mock
    private MedicalClinicClient client;
    @InjectMocks
    private ProxyService proxyService;

    @Test
    void getPatientVisits_Success_ReturnVisits() {
        VisitDto dto = new VisitDto();
        dto.setId(10L);
        when(client.getPatientVisits(1L)).thenReturn(new PageResponse<>(List.of(dto), 0, 20, 1L, 1));
        List<VisitResponse> result = proxyService.getPatientVisits(1L);
        assertEquals(1, result.size());
        assertEquals(10L, result.getFirst().getId());
        verify(client).getPatientVisits(1L);
    }

    @Test
    void bookVisit_Success_ReturnBookedVisit() {
        VisitDto dto = new VisitDto();
        dto.setId(10L);
        when(client.bookVisit(1L, 2L)).thenReturn(dto);
        VisitResponse result = proxyService.bookVisit(1L, 2L);
        assertEquals(10L, result.getId());
        verify(client).bookVisit(1L, 2L);
    }

    @Test
    void getAvailableVisitsByDoctor_Success_ReturnVisits() {
        VisitDto dto = new VisitDto();
        dto.setId(15L);
        when(client.getAvailableVisitsByDoctor(1L, true)).thenReturn(new PageResponse<>(List.of(dto), 0, 20, 1L, 1));
        List<VisitResponse> result = proxyService.getAvailableVisitsByDoctor(1L);
        assertEquals(1, result.size());
        assertEquals(15L, result.getFirst().getId());
        verify(client).getAvailableVisitsByDoctor(1L, true);
    }

    @Test
    void getAvailableVisitsBySpecializationAndDate_Success_ReturnVisits() {
        LocalDate date = LocalDate.of(2026, 8, 18);
        VisitDto dto = new VisitDto();
        dto.setId(20L);
        when(client.getAvailableVisitsBySpecializationAndDate(Specialization.CARDIOLOGY, date, true))
                .thenReturn(new PageResponse<>(List.of(dto), 0, 20, 1L, 1));
        List<VisitResponse> result = proxyService.getAvailableVisitsBySpecializationAndDate(Specialization.CARDIOLOGY, date);
        assertEquals(1, result.size());
        assertEquals(20L, result.getFirst().getId());
        verify(client).getAvailableVisitsBySpecializationAndDate(Specialization.CARDIOLOGY, date, true);
    }

    @Test
    void getDoctorVisits_Success_ReturnVisits() {
        VisitDto dto = new VisitDto();
        dto.setId(25L);
        when(client.getDoctorVisits(1L)).thenReturn(new PageResponse<>(List.of(dto), 0, 20, 1L, 1));
        List<VisitResponse> result = proxyService.getDoctorVisits(1L);
        assertEquals(1, result.size());
        assertEquals(25L, result.getFirst().getId());
        verify(client).getDoctorVisits(1L);
    }

    @Test
    void cancelVisit_Success_ReturnCancelledVisit() {
        VisitDto dto = new VisitDto();
        dto.setId(1L);
        when(client.cancelVisit(1L)).thenReturn(dto);
        VisitResponse result = proxyService.cancelVisit(1L);
        assertEquals(1L, result.getId());
        verify(client).cancelVisit(1L);
    }

    @Test
    void getAvailableVisitsByDateRange_Success_ReturnVisits() {
        LocalDate start = LocalDate.of(2026, 8, 18);
        LocalDate end = LocalDate.of(2026, 8, 25);
        VisitDto dto = new VisitDto();
        dto.setId(30L);
        when(client.getAvailableVisitsByDateRange(Specialization.CARDIOLOGY, start, end, true))
                .thenReturn(new PageResponse<>(List.of(dto), 0, 20, 1L, 1));
        List<VisitResponse> result = proxyService.getAvailableVisitsByDateRange(Specialization.CARDIOLOGY, start, end);
        assertEquals(1, result.size());
        assertEquals(30L, result.getFirst().getId());
        verify(client).getAvailableVisitsByDateRange(Specialization.CARDIOLOGY, start, end, true);
    }

    @Test
    void getVisitsBySpecializationAndDateRange_Success_ReturnVisits() {
        LocalDate start = LocalDate.of(2026, 8, 18);
        LocalDate end = LocalDate.of(2026, 8, 25);
        VisitDto dto = new VisitDto();
        dto.setId(35L);
        when(client.getVisitsBySpecializationAndDateRange(Specialization.CARDIOLOGY, start, end))
                .thenReturn(new PageResponse<>(List.of(dto), 0, 20, 1L, 1));
        List<VisitResponse> result = proxyService.getVisitsBySpecializationAndDateRange(Specialization.CARDIOLOGY, start, end);
        assertEquals(1, result.size());
        assertEquals(35L, result.getFirst().getId());
        verify(client).getVisitsBySpecializationAndDateRange(Specialization.CARDIOLOGY, start, end);
    }

    @Test
    void getDoctorsBySpecialization_Success_ReturnDoctors() {
        DoctorDto dto = new DoctorDto();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setSpecialization(Specialization.CARDIOLOGY);
        when(client.getDoctorsBySpecialization(Specialization.CARDIOLOGY)).thenReturn(List.of(dto));
        List<DoctorResponse> result = proxyService.getDoctorsBySpecialization(Specialization.CARDIOLOGY);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
        assertEquals("John", result.getFirst().getFirstName());
        assertEquals("Doe", result.getFirst().getLastName());
        assertEquals(Specialization.CARDIOLOGY, result.getFirst().getSpecialization());
        verify(client).getDoctorsBySpecialization(Specialization.CARDIOLOGY);
    }

    @Test
    void getPatientVisits_FallbackReturnsEmptyList_ReturnEmptyList() {
        when(client.getPatientVisits(99L)).thenReturn(new PageResponse<>(Collections.emptyList(), 0, 0, 0L, 0));
        assertTrue(proxyService.getPatientVisits(99L).isEmpty());
        verify(client).getPatientVisits(99L);
    }

    @Test
    void bookVisit_ClientThrowsServiceUnavailable_PropagatesException() {
        when(client.bookVisit(1L, 2L)).thenThrow(new ServiceUnavailableException("Failed to book visit, service unavailable"));
        assertThrows(ServiceUnavailableException.class, () -> proxyService.bookVisit(1L, 2L));
        verify(client).bookVisit(1L, 2L);
    }

    @Test
    void cancelVisit_ClientThrowsServiceUnavailable_PropagatesException() {
        when(client.cancelVisit(1L)).thenThrow(new ServiceUnavailableException("Failed to cancel visit, service unavailable"));
        assertThrows(ServiceUnavailableException.class, () -> proxyService.cancelVisit(1L));
        verify(client).cancelVisit(1L);
    }

    @Test
    void getAvailableVisitsByDoctor_FallbackReturnsEmptyList_ReturnEmptyList() {
        when(client.getAvailableVisitsByDoctor(99L, true)).thenReturn(new PageResponse<>(Collections.emptyList(), 0, 0, 0L, 0));
        assertTrue(proxyService.getAvailableVisitsByDoctor(99L).isEmpty());
        verify(client).getAvailableVisitsByDoctor(99L, true);
    }

    @Test
    void getDoctorVisits_FallbackReturnsEmptyList_ReturnEmptyList() {
        when(client.getDoctorVisits(99L)).thenReturn(new PageResponse<>(Collections.emptyList(), 0, 0, 0L, 0));
        assertTrue(proxyService.getDoctorVisits(99L).isEmpty());
        verify(client).getDoctorVisits(99L);
    }

    @Test
    void getAvailableVisitsBySpecializationAndDate_FallbackReturnsEmptyList_ReturnEmptyList() {
        LocalDate date = LocalDate.of(2026, 8, 18);
        when(client.getAvailableVisitsBySpecializationAndDate(Specialization.NEUROLOGIST, date, true))
                .thenReturn(new PageResponse<>(Collections.emptyList(), 0, 0, 0L, 0));
        assertTrue(proxyService.getAvailableVisitsBySpecializationAndDate(Specialization.NEUROLOGIST, date).isEmpty());
        verify(client).getAvailableVisitsBySpecializationAndDate(Specialization.NEUROLOGIST, date, true);
    }

    @Test
    void getAvailableVisitsByDateRange_WithoutSpecialization_ReturnEmptyList() {
        LocalDate start = LocalDate.of(2026, 8, 18);
        LocalDate end = LocalDate.of(2026, 8, 25);
        when(client.getAvailableVisitsByDateRange(null, start, end, true))
                .thenReturn(new PageResponse<>(Collections.emptyList(), 0, 0, 0L, 0));
        assertTrue(proxyService.getAvailableVisitsByDateRange(null, start, end).isEmpty());
        verify(client).getAvailableVisitsByDateRange(null, start, end, true);
    }

    @Test
    void getVisitsBySpecializationAndDateRange_FallbackReturnsEmptyList_ReturnEmptyList() {
        LocalDate start = LocalDate.of(2026, 8, 18);
        LocalDate end = LocalDate.of(2026, 8, 25);
        when(client.getVisitsBySpecializationAndDateRange(Specialization.SURGEON, start, end))
                .thenReturn(new PageResponse<>(Collections.emptyList(), 0, 0, 0L, 0));
        assertTrue(proxyService.getVisitsBySpecializationAndDateRange(Specialization.SURGEON, start, end).isEmpty());
        verify(client).getVisitsBySpecializationAndDateRange(Specialization.SURGEON, start, end);
    }

    @Test
    void getDoctorsBySpecialization_FallbackReturnsEmptyList_ReturnEmptyList() {
        when(client.getDoctorsBySpecialization(Specialization.DERMATOLOGY)).thenReturn(Collections.emptyList());
        assertTrue(proxyService.getDoctorsBySpecialization(Specialization.DERMATOLOGY).isEmpty());
        verify(client).getDoctorsBySpecialization(Specialization.DERMATOLOGY);
    }
}

