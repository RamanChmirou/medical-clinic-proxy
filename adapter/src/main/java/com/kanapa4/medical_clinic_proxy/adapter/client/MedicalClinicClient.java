package com.kanapa4.medical_clinic_proxy.adapter.client;

import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.PageResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(
    name = "medical-clinic-client",
    fallbackFactory = MedicalClinicClientFallback.class
)
public interface MedicalClinicClient {

    @GetMapping("/visits")
    PageResponse<VisitDto> getVisits(
            @RequestParam(value = "patientId", required = false) Long patientId,
            @RequestParam(value = "doctorId", required = false) Long doctorId,
            @RequestParam(value = "specialization", required = false) Specialization specialization,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "available", required = false) Boolean available);

    @PatchMapping("/visits/{visitId}/book/{patientId}")
    VisitDto bookVisit(@PathVariable("visitId") Long visitId, @PathVariable("patientId") Long patientId);

    @PatchMapping("/visits/{visitId}/cancel")
    VisitDto cancelVisit(@PathVariable("visitId") Long visitId);

    @GetMapping("/doctors")
    PageResponse<DoctorDto> getDoctorsBySpecialization(@RequestParam("specialization") Specialization specialization);
}
