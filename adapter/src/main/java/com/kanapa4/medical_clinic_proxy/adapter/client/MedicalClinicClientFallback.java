package com.kanapa4.medical_clinic_proxy.adapter.client;

import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.PageResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import com.kanapa4.medical_clinic_proxy.model.exception.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class MedicalClinicClientFallback implements FallbackFactory<MedicalClinicClient> {

    private static final PageResponse<VisitDto> EMPTY_VISIT_PAGE =
            new PageResponse<>(Collections.emptyList(), 0, 0, 0L, 0);

    @Override
    public MedicalClinicClient create(Throwable cause) {
        return new MedicalClinicClient() {

            @Override
            public PageResponse<VisitDto> getVisits(Long patientId, Long doctorId, Specialization specialization, LocalDate startDate, LocalDate endDate, Boolean available) {
                log.error("Fallback for getVisits triggered due to error: {}", cause.getMessage(), cause);
                return EMPTY_VISIT_PAGE;
            }

            @Override
            public VisitDto bookVisit(Long visitId, Long patientId) {
                log.error("Fallback for bookVisit triggered due to error: {}", cause.getMessage(), cause);
                throw new ServiceUnavailableException("Failed to book visit, service unavailable", cause);
            }

            @Override
            public VisitDto cancelVisit(Long visitId) {
                log.error("Fallback for cancelVisit triggered due to error: {}", cause.getMessage(), cause);
                throw new ServiceUnavailableException("Failed to cancel visit, service unavailable", cause);
            }

            @Override
            public List<DoctorDto> getDoctorsBySpecialization(Specialization specialization) {
                log.error("Fallback for getDoctorsBySpecialization triggered due to error: {}", cause.getMessage(), cause);
                return Collections.emptyList();
            }
        };
    }
}
