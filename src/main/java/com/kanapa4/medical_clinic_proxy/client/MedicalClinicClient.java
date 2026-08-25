package com.kanapa4.medical_clinic_proxy.client;

import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.PageResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@FeignClient(
    name = "medical-clinic-client",
    fallbackFactory = MedicalClinicClientFallback.class
)
public interface MedicalClinicClient {

    @GetMapping("/visits")
    PageResponse<VisitDto> getPatientVisits(@RequestParam("patientId") Long patientId);

    @PatchMapping("/visits/{visitId}/book/{patientId}")
    VisitDto bookVisit(@PathVariable("visitId") Long visitId, @PathVariable("patientId") Long patientId);

    @GetMapping("/visits")
    PageResponse<VisitDto> getAvailableVisitsByDoctor(
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("available") boolean available);

    @GetMapping("/visits")
    PageResponse<VisitDto> getAvailableVisitsBySpecializationAndDate(
            @RequestParam("specialization") Specialization specialization,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("available") boolean available);

    @GetMapping("/visits")
    PageResponse<VisitDto> getDoctorVisits(@RequestParam("doctorId") Long doctorId);

    @PatchMapping("/visits/{visitId}/cancel")
    VisitDto cancelVisit(@PathVariable("visitId") Long visitId);

    @GetMapping("/visits")
    PageResponse<VisitDto> getAvailableVisitsByDateRange(
            @RequestParam(value = "specialization", required = false) Specialization specialization,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("available") boolean available);

    @GetMapping("/visits")
    PageResponse<VisitDto> getVisitsBySpecializationAndDateRange(
            @RequestParam("specialization") Specialization specialization,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);

    @GetMapping("/doctors/specialization/{specialization}")
    List<DoctorDto> getDoctorsBySpecialization(@PathVariable("specialization") Specialization specialization);
}
