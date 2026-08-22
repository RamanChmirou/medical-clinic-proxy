package com.kanapa4.medical_clinic_proxy.client;

import com.kanapa4.medical_clinic_proxy.exception.ServiceUnavailableException;
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

    @Override
    public MedicalClinicClient create(Throwable cause) {
        return new MedicalClinicClient() {

            @Override
            public List<VisitDto> getPatientVisits(Long patientId) {
                log.error("Fallback for getPatientVisits triggered due to error: {}", cause.getMessage(), cause);
                return Collections.emptyList();
            }

            @Override
            public VisitDto bookVisit(Long visitId, Long patientId) {
                log.error("Fallback for bookVisit triggered due to error: {}", cause.getMessage(), cause);
                throw new ServiceUnavailableException("Failed to book visit, service unavailable");
            }

            @Override
            public List<VisitDto> getAvailableVisitsByDoctor(Long doctorId) {
                log.error("Fallback for getAvailableVisitsByDoctor triggered due to error: {}", cause.getMessage(), cause);
                return Collections.emptyList();
            }

            @Override
            public List<VisitDto> getAvailableVisitsBySpecializationAndDate(Specialization specialization, LocalDate date) {
                log.error("Fallback for getAvailableVisitsBySpecializationAndDate triggered due to error: {}", cause.getMessage(), cause);
                return Collections.emptyList();
            }
        };
    }
}
