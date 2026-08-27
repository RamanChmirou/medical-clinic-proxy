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
    void getVisits_Success_ReturnVisits() {
        VisitDto dto = new VisitDto();
        dto.setId(10L);
        when(client.getVisits(1L, null, null, null, null, false))
                .thenReturn(new PageResponse<>(List.of(dto), 0, 20, 1L, 1));
        List<VisitResponse> result = proxyService.getVisits(1L, null, null, null, null, false);
        assertEquals(1, result.size());
        assertEquals(10L, result.getFirst().getId());
        verify(client).getVisits(1L, null, null, null, null, false);
    }

    @Test
    void getVisits_AvailableOnly_Success_ReturnVisits() {
        VisitDto dto = new VisitDto();
        dto.setId(15L);
        when(client.getVisits(null, 2L, null, null, null, true))
                .thenReturn(new PageResponse<>(List.of(dto), 0, 20, 1L, 1));
        List<VisitResponse> result = proxyService.getVisits(null, 2L, null, null, null, true);
        assertEquals(1, result.size());
        assertEquals(15L, result.getFirst().getId());
        verify(client).getVisits(null, 2L, null, null, null, true);
    }

    @Test
    void getVisits_WithSpecializationAndDateRange_Success_ReturnVisits() {
        LocalDate start = LocalDate.of(2026, 8, 18);
        LocalDate end = LocalDate.of(2026, 8, 25);
        VisitDto dto = new VisitDto();
        dto.setId(20L);
        when(client.getVisits(null, null, Specialization.CARDIOLOGY, start, end, true))
                .thenReturn(new PageResponse<>(List.of(dto), 0, 20, 1L, 1));
        List<VisitResponse> result = proxyService.getVisits(null, null, Specialization.CARDIOLOGY, start, end, true);
        assertEquals(1, result.size());
        assertEquals(20L, result.getFirst().getId());
        verify(client).getVisits(null, null, Specialization.CARDIOLOGY, start, end, true);
    }

    @Test
    void getVisits_FallbackReturnsEmptyList_ReturnEmptyList() {
        when(client.getVisits(99L, null, null, null, null, false))
                .thenReturn(new PageResponse<>(Collections.emptyList(), 0, 0, 0L, 0));
        assertTrue(proxyService.getVisits(99L, null, null, null, null, false).isEmpty());
        verify(client).getVisits(99L, null, null, null, null, false);
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
    void cancelVisit_Success_ReturnCancelledVisit() {
        VisitDto dto = new VisitDto();
        dto.setId(1L);
        when(client.cancelVisit(1L)).thenReturn(dto);
        VisitResponse result = proxyService.cancelVisit(1L);
        assertEquals(1L, result.getId());
        verify(client).cancelVisit(1L);
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
    void getDoctorsBySpecialization_FallbackReturnsEmptyList_ReturnEmptyList() {
        when(client.getDoctorsBySpecialization(Specialization.DERMATOLOGY)).thenReturn(Collections.emptyList());
        assertTrue(proxyService.getDoctorsBySpecialization(Specialization.DERMATOLOGY).isEmpty());
        verify(client).getDoctorsBySpecialization(Specialization.DERMATOLOGY);
    }
}
