package com.kanapa4.medical_clinic_proxy.domain;

import com.kanapa4.medical_clinic_proxy.domain.port.MedicalClinicClientPort;
import com.kanapa4.medical_clinic_proxy.domain.port.MedicalClinicMapperPort;
import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.PageResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import com.kanapa4.medical_clinic_proxy.pub.DoctorResponse;
import com.kanapa4.medical_clinic_proxy.pub.VisitResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ProxyService {
    private final MedicalClinicClientPort clientPort;
    private final MedicalClinicMapperPort mapperPort;

    public List<VisitResponse> getVisits(Long patientId, Long doctorId, Specialization specialization, LocalDate startDate, LocalDate endDate, Boolean availableOnly) {
        log.info("Fetching visits with filters: patientId={}, doctorId={}, specialization={}, startDate={}, endDate={}, availableOnly={}", patientId, doctorId, specialization, startDate, endDate, availableOnly);
        PageResponse<VisitDto> page = clientPort.getVisits(patientId, doctorId, specialization, startDate, endDate, availableOnly);
        return mapperPort.toVisitResponses(page.content());
    }

    public VisitResponse bookVisit(Long visitId, Long patientId) {
        log.info("Booking visit {} for patient {}", visitId, patientId);
        VisitDto dto = clientPort.bookVisit(visitId, patientId);
        return mapperPort.toVisitResponse(dto);
    }

    public VisitResponse cancelVisit(Long visitId) {
        log.info("Cancelling visit {}", visitId);
        VisitDto dto = clientPort.cancelVisit(visitId);
        return mapperPort.toVisitResponse(dto);
    }

    public List<DoctorResponse> getDoctorsBySpecialization(Specialization specialization) {
        log.info("Fetching doctors for specialization {}", specialization);
        PageResponse<DoctorDto> page = clientPort.getDoctorsBySpecialization(specialization);
        return mapperPort.toDoctorResponses(page.content());
    }
}
