package com.kanapa4.medical_clinic_proxy.service;

import com.kanapa4.medical_clinic_proxy.client.MedicalClinicClient;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import com.kanapa4.medical_clinic_proxy.model.VisitResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
}
