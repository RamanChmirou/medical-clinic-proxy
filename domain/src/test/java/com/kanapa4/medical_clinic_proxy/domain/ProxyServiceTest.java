package com.kanapa4.medical_clinic_proxy.domain;

import com.kanapa4.medical_clinic_proxy.domain.port.MedicalClinicClientPort;
import com.kanapa4.medical_clinic_proxy.domain.port.MedicalClinicMapperPort;
import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.PageResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import com.kanapa4.medical_clinic_proxy.model.exception.ServiceUnavailableException;
import com.kanapa4.medical_clinic_proxy.pub.DoctorResponse;
import com.kanapa4.medical_clinic_proxy.pub.VisitResponse;
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
class ProxyServiceTest {

    @Mock
    private MedicalClinicClientPort clientPort;

    @Mock
    private MedicalClinicMapperPort mapperPort;

    @InjectMocks
    private ProxyService proxyService;

    @Test
    void getVisits_Success_ReturnVisits() {
        VisitDto dto = new VisitDto();
        dto.setId(10L);
        VisitResponse response = VisitResponse.builder().id(10L).build();

        when(clientPort.getVisits(1L, null, null, null, null, false))
                .thenReturn(new PageResponse<>(List.of(dto), 0, 20, 1L, 1));
        when(mapperPort.toVisitResponses(List.of(dto))).thenReturn(List.of(response));

        List<VisitResponse> result = proxyService.getVisits(1L, null, null, null, null, false);

        assertEquals(1, result.size());
        assertEquals(10L, result.getFirst().getId());
        verify(clientPort).getVisits(1L, null, null, null, null, false);
        verify(mapperPort).toVisitResponses(List.of(dto));
    }

    @Test
    void getVisits_AvailableOnly_Success_ReturnVisits() {
        VisitDto dto = new VisitDto();
        dto.setId(15L);
        VisitResponse response = VisitResponse.builder().id(15L).build();

        when(clientPort.getVisits(null, 2L, null, null, null, true))
                .thenReturn(new PageResponse<>(List.of(dto), 0, 20, 1L, 1));
        when(mapperPort.toVisitResponses(List.of(dto))).thenReturn(List.of(response));

        List<VisitResponse> result = proxyService.getVisits(null, 2L, null, null, null, true);

        assertEquals(1, result.size());
        assertEquals(15L, result.getFirst().getId());
        verify(clientPort).getVisits(null, 2L, null, null, null, true);
        verify(mapperPort).toVisitResponses(List.of(dto));
    }

    @Test
    void getVisits_WithSpecializationAndDateRange_Success_ReturnVisits() {
        LocalDate start = LocalDate.of(2026, 8, 18);
        LocalDate end = LocalDate.of(2026, 8, 25);
        VisitDto dto = new VisitDto();
        dto.setId(20L);
        VisitResponse response = VisitResponse.builder().id(20L).build();

        when(clientPort.getVisits(null, null, Specialization.CARDIOLOGY, start, end, true))
                .thenReturn(new PageResponse<>(List.of(dto), 0, 20, 1L, 1));
        when(mapperPort.toVisitResponses(List.of(dto))).thenReturn(List.of(response));

        List<VisitResponse> result = proxyService.getVisits(null, null, Specialization.CARDIOLOGY, start, end, true);

        assertEquals(1, result.size());
        assertEquals(20L, result.getFirst().getId());
        verify(clientPort).getVisits(null, null, Specialization.CARDIOLOGY, start, end, true);
        verify(mapperPort).toVisitResponses(List.of(dto));
    }

    @Test
    void getVisits_FallbackReturnsEmptyList_ReturnEmptyList() {
        when(clientPort.getVisits(99L, null, null, null, null, false))
                .thenReturn(new PageResponse<>(Collections.emptyList(), 0, 0, 0L, 0));
        when(mapperPort.toVisitResponses(Collections.emptyList())).thenReturn(Collections.emptyList());

        assertTrue(proxyService.getVisits(99L, null, null, null, null, false).isEmpty());
        verify(clientPort).getVisits(99L, null, null, null, null, false);
        verify(mapperPort).toVisitResponses(Collections.emptyList());
    }

    @Test
    void bookVisit_Success_ReturnBookedVisit() {
        VisitDto dto = new VisitDto();
        dto.setId(10L);
        VisitResponse response = VisitResponse.builder().id(10L).build();

        when(clientPort.bookVisit(1L, 2L)).thenReturn(dto);
        when(mapperPort.toVisitResponse(dto)).thenReturn(response);

        VisitResponse result = proxyService.bookVisit(1L, 2L);

        assertEquals(10L, result.getId());
        verify(clientPort).bookVisit(1L, 2L);
        verify(mapperPort).toVisitResponse(dto);
    }

    @Test
    void cancelVisit_Success_ReturnCancelledVisit() {
        VisitDto dto = new VisitDto();
        dto.setId(1L);
        VisitResponse response = VisitResponse.builder().id(1L).build();

        when(clientPort.cancelVisit(1L)).thenReturn(dto);
        when(mapperPort.toVisitResponse(dto)).thenReturn(response);

        VisitResponse result = proxyService.cancelVisit(1L);

        assertEquals(1L, result.getId());
        verify(clientPort).cancelVisit(1L);
        verify(mapperPort).toVisitResponse(dto);
    }

    @Test
    void bookVisit_ClientThrowsServiceUnavailable_PropagatesException() {
        when(clientPort.bookVisit(1L, 2L)).thenThrow(new ServiceUnavailableException("Failed to book visit, service unavailable"));
        assertThrows(ServiceUnavailableException.class, () -> proxyService.bookVisit(1L, 2L));
        verify(clientPort).bookVisit(1L, 2L);
    }

    @Test
    void cancelVisit_ClientThrowsServiceUnavailable_PropagatesException() {
        when(clientPort.cancelVisit(1L)).thenThrow(new ServiceUnavailableException("Failed to cancel visit, service unavailable"));
        assertThrows(ServiceUnavailableException.class, () -> proxyService.cancelVisit(1L));
        verify(clientPort).cancelVisit(1L);
    }

    @Test
    void getDoctorsBySpecialization_Success_ReturnDoctors() {
        DoctorDto dto = new DoctorDto();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setSpecialization(Specialization.CARDIOLOGY);

        DoctorResponse response = DoctorResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .specialization(Specialization.CARDIOLOGY)
                .build();

        PageResponse<DoctorDto> pageResponse = new PageResponse<>(
                List.of(dto),
                1,
                0,
                1L,
                1
        );

        when(clientPort.getDoctorsBySpecialization(Specialization.CARDIOLOGY))
                .thenReturn(pageResponse);
        when(mapperPort.toDoctorResponses(List.of(dto))).thenReturn(List.of(response));

        List<DoctorResponse> result = proxyService.getDoctorsBySpecialization(Specialization.CARDIOLOGY);

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
        assertEquals("John", result.getFirst().getFirstName());
        assertEquals("Doe", result.getFirst().getLastName());
        assertEquals(Specialization.CARDIOLOGY, result.getFirst().getSpecialization());
        verify(clientPort).getDoctorsBySpecialization(Specialization.CARDIOLOGY);
        verify(mapperPort).toDoctorResponses(List.of(dto));
    }

    @Test
    void getDoctorsBySpecialization_FallbackReturnsEmptyList_ReturnEmptyList() {
        PageResponse<DoctorDto> emptyPage = new PageResponse<>(
                Collections.emptyList(),
                0,
                0,
                0L,
                0
        );

        when(clientPort.getDoctorsBySpecialization(Specialization.DERMATOLOGY))
                .thenReturn(emptyPage);
        when(mapperPort.toDoctorResponses(Collections.emptyList())).thenReturn(Collections.emptyList());

        assertTrue(proxyService.getDoctorsBySpecialization(Specialization.DERMATOLOGY).isEmpty());
        verify(clientPort).getDoctorsBySpecialization(Specialization.DERMATOLOGY);
        verify(mapperPort).toDoctorResponses(Collections.emptyList());
    }
}
