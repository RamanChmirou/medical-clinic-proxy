package com.kanapa4.medical_clinic_proxy.service;

import com.kanapa4.medical_clinic_proxy.client.MedicalClinicClient;
import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.DoctorResponse;
import com.kanapa4.medical_clinic_proxy.model.DoctorPageResponse;
import com.kanapa4.medical_clinic_proxy.model.PageResponse;
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
        return mapVisitsToResponse(client.getPatientVisits(patientId));
    }

    public VisitResponse bookVisit(Long visitId, Long patientId) {
        log.info("Booking visit {} for patient {}", visitId, patientId);
        return mapToVisitResponse(client.bookVisit(visitId, patientId));
    }

    public List<VisitResponse> getAvailableVisitsByDoctor(Long doctorId) {
        log.info("Fetching available visits for doctor {}", doctorId);
        return mapVisitsToResponse(client.getAvailableVisitsByDoctor(doctorId));
    }

    public List<VisitResponse> getAvailableVisitsBySpecializationAndDate(Specialization specialization, LocalDate date) {
        log.info("Fetching available visits for spec {} and date {}", specialization, date);
        return mapVisitsToResponse(client.getAvailableVisitsBySpecializationAndDate(specialization, date));
    }

    public List<VisitResponse> getDoctorVisits(Long doctorId) {
        log.info("Fetching all visits for doctor {}", doctorId);
        return mapVisitsToResponse(client.getDoctorVisits(doctorId));
    }

    public VisitResponse cancelVisit(Long visitId) {
        log.info("Cancelling visit {}", visitId);
        return mapToVisitResponse(client.cancelVisit(visitId));
    }

    public List<VisitResponse> getAvailableVisitsByDateRange(Specialization specialization, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching available visits for spec {} from {} to {}", specialization, startDate, endDate);
        return mapVisitsToResponse(client.getAvailableVisitsByDateRange(specialization, startDate, endDate));
    }

    public List<VisitResponse> getVisitsBySpecializationAndDateRange(Specialization specialization, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching visits for spec {} from {} to {}", specialization, startDate, endDate);
        return mapVisitsToResponse(client.getVisitsBySpecializationAndDateRange(specialization, startDate, endDate));
    }

    public PageResponse<DoctorResponse> getDoctorsBySpecialization(Specialization specialization, int page, int size) {
        log.info("Fetching doctors for specialization {}", specialization);
        DoctorPageResponse clientResponse = client.getDoctorsBySpecialization(specialization, page, size);
        List<DoctorResponse> content = clientResponse.getContent().stream()
                .map(this::mapToDoctorResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(
                content,
                clientResponse.getNumber() != null ? clientResponse.getNumber() : 0,
                clientResponse.getSize() != null ? clientResponse.getSize() : 10,
                clientResponse.getTotalElements() != null ? clientResponse.getTotalElements() : 0L,
                clientResponse.getTotalPages() != null ? clientResponse.getTotalPages() : 0
        );
    }

    private List<VisitResponse> mapVisitsToResponse(List<VisitDto> dtos) {
        return dtos.stream().map(this::mapToVisitResponse).collect(Collectors.toList());
    }

    private VisitResponse mapToVisitResponse(VisitDto dto) {
        return VisitResponse.builder()
                .id(dto.getId())
                .dateTime(dto.getDateTime())
                .durationInMinutes(dto.getDurationInMinutes())
                .doctorId(dto.getDoctorId())
                .patientId(dto.getPatientId())
                .facilityId(dto.getFacilityId())
                .build();
    }

    private DoctorResponse mapToDoctorResponse(DoctorDto dto) {
        return DoctorResponse.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .specialization(dto.getSpecialization())
                .build();
    }
}
