package com.kanapa4.medical_clinic_proxy.service;

import com.kanapa4.medical_clinic_proxy.client.MedicalClinicClient;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import com.kanapa4.medical_clinic_proxy.model.VisitResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyService {
    private final MedicalClinicClient client;

    public List<VisitResponse> getPatientVisits(Long patientId) {
        log.info("Fetching visits for patient {}", patientId);
        return mapToResponse(client.getPatientVisits(patientId));
    }

    public VisitResponse bookVisit(Long visitId, Long patientId) {
        log.info("Booking visit {} for patient {}", visitId, patientId);
        return mapToResponse(client.bookVisit(visitId, patientId));
    }

    public List<VisitResponse> getAvailableVisitsByDoctor(Long doctorId) {
        log.info("Fetching available visits for doctor {}", doctorId);
        return mapToResponse(client.getAvailableVisitsByDoctor(doctorId));
    }

    public List<VisitResponse> getAvailableVisitsBySpecializationAndDate(Specialization specialization, LocalDate date) {
        log.info("Fetching available visits for spec {} and date {}", specialization, date);
        return mapToResponse(client.getAvailableVisitsBySpecializationAndDate(specialization, date));
    }

    private List<VisitResponse> mapToResponse(List<VisitDto> dtos) {
        return dtos.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private VisitResponse mapToResponse(VisitDto dto) {
        return VisitResponse.builder()
                .id(dto.getId())
                .dateTime(dto.getDateTime())
                .durationInMinutes(dto.getDurationInMinutes())
                .doctorId(dto.getDoctorId())
                .patientId(dto.getPatientId())
                .facilityId(dto.getFacilityId())
                .build();
    }
}
