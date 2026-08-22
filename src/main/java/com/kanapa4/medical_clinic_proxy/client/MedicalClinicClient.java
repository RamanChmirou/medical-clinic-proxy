package com.kanapa4.medical_clinic_proxy.client;

import com.kanapa4.medical_clinic_proxy.config.FeignRetryConfig;
import com.kanapa4.medical_clinic_proxy.model.DoctorPageResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@FeignClient(
    name = "medical-clinic-client",
    url = "${spring.cloud.openfeign.client.config.medical-clinic-client.url}",
    fallbackFactory = MedicalClinicClientFallback.class,
    configuration = FeignRetryConfig.class
)

public interface MedicalClinicClient {

    @GetMapping("/visits/patient/{patientId}")
    List<VisitDto> getPatientVisits(@PathVariable("patientId") Long patientId);

    @PatchMapping("/visits/{visitId}/book/{patientId}")
    VisitDto bookVisit(@PathVariable("visitId") Long visitId, @PathVariable("patientId") Long patientId);

    @GetMapping("/visits/available/doctor/{doctorId}")
    List<VisitDto> getAvailableVisitsByDoctor(@PathVariable("doctorId") Long doctorId);

    @GetMapping("/visits/available")
    List<VisitDto> getAvailableVisitsBySpecializationAndDate(
            @RequestParam("specialization") Specialization specialization,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);
    @GetMapping("/visits/doctor/{doctorId}")
    List<VisitDto> getDoctorVisits(@PathVariable("doctorId") Long doctorId);

    @PatchMapping("/visits/{visitId}/cancel")
    VisitDto cancelVisit(@PathVariable("visitId") Long visitId);

    @GetMapping("/visits/available-range")
    List<VisitDto> getAvailableVisitsByDateRange(
            @RequestParam(value = "specialization", required = false) Specialization specialization,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
    @GetMapping("/visits/search")
    List<VisitDto> getVisitsBySpecializationAndDateRange(
            @RequestParam("specialization") Specialization specialization,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
    @GetMapping("/doctors")
    DoctorPageResponse getDoctorsBySpecialization(
            @RequestParam(value = "specialization", required = false) Specialization specialization,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size);
}
