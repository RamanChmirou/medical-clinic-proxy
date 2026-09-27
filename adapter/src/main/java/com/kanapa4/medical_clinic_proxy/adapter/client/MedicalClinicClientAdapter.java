package com.kanapa4.medical_clinic_proxy.adapter.client;

import com.kanapa4.medical_clinic_proxy.domain.port.MedicalClinicClientPort;
import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.PageResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MedicalClinicClientAdapter implements MedicalClinicClientPort {

    private final MedicalClinicClient client;

    @Override
    public PageResponse<VisitDto> getVisits(Long patientId, Long doctorId, Specialization specialization, LocalDate startDate, LocalDate endDate, Boolean available) {
        return client.getVisits(patientId, doctorId, specialization, startDate, endDate, available);
    }

    @Override
    public VisitDto bookVisit(Long visitId, Long patientId) {
        return client.bookVisit(visitId, patientId);
    }

    @Override
    public VisitDto cancelVisit(Long visitId) {
        return client.cancelVisit(visitId);
    }

    @Override
    public List<DoctorDto> getDoctorsBySpecialization(Specialization specialization) {
        return client.getDoctorsBySpecialization(specialization);
    }
}
