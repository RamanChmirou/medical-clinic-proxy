package com.kanapa4.medical_clinic_proxy.client;

import com.kanapa4.medical_clinic_proxy.exception.ServiceUnavailableException;
import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.PageResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
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
            public PageResponse<VisitDto> getPatientVisits(Long patientId) {
                log.error("Fallback for getPatientVisits triggered due to error: {}", cause.getMessage(), cause);
                return EMPTY_VISIT_PAGE;
            }

            @Override
            public VisitDto bookVisit(Long visitId, Long patientId) {
                log.error("Fallback for bookVisit triggered due to error: {}", cause.getMessage(), cause);
                throw new ServiceUnavailableException("Failed to book visit, service unavailable");
            }

            @Override
            public PageResponse<VisitDto> getAvailableVisitsByDoctor(Long doctorId, boolean available) {
                log.error("Fallback for getAvailableVisitsByDoctor triggered due to error: {}", cause.getMessage(), cause);
                return EMPTY_VISIT_PAGE;
            }

            @Override
            public PageResponse<VisitDto> getAvailableVisitsBySpecializationAndDate(Specialization specialization, LocalDate date, boolean available) {
                log.error("Fallback for getAvailableVisitsBySpecializationAndDate triggered due to error: {}", cause.getMessage(), cause);
                return EMPTY_VISIT_PAGE;
            }

            @Override
            public PageResponse<VisitDto> getDoctorVisits(Long doctorId) {
                log.error("Fallback for getDoctorVisits triggered due to error: {}", cause.getMessage(), cause);
                return EMPTY_VISIT_PAGE;
            }

            @Override
            public VisitDto cancelVisit(Long visitId) {
                log.error("Fallback for cancelVisit triggered due to error: {}", cause.getMessage(), cause);
                throw new ServiceUnavailableException("Failed to cancel visit, service unavailable");
            }

            @Override
            public PageResponse<VisitDto> getAvailableVisitsByDateRange(Specialization specialization, LocalDate startDate, LocalDate endDate, boolean available) {
                log.error("Fallback for getAvailableVisitsByDateRange triggered due to error: {}", cause.getMessage(), cause);
                return EMPTY_VISIT_PAGE;
            }

            @Override
            public PageResponse<VisitDto> getVisitsBySpecializationAndDateRange(Specialization specialization, LocalDate startDate, LocalDate endDate) {
                log.error("Fallback for getVisitsBySpecializationAndDateRange triggered due to error: {}", cause.getMessage(), cause);
                return EMPTY_VISIT_PAGE;
            }

            @Override
            public List<DoctorDto> getDoctorsBySpecialization(Specialization specialization) {
                log.error("Fallback for getDoctorsBySpecialization triggered due to error: {}", cause.getMessage(), cause);
                return Collections.emptyList();
            }
        };
    }
}
