package com.kanapa4.medical_clinic_proxy.domain.port;

import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.PageResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import java.time.LocalDate;

public interface MedicalClinicClientPort {

    PageResponse<VisitDto> getVisits(Long patientId, Long doctorId, Specialization specialization, LocalDate startDate, LocalDate endDate, Boolean available);

    VisitDto bookVisit(Long visitId, Long patientId);

    VisitDto cancelVisit(Long visitId);

    PageResponse<DoctorDto> getDoctorsBySpecialization(Specialization specialization);
}
