package com.kanapa4.medical_clinic_proxy.controller;

import com.kanapa4.medical_clinic_proxy.model.BookVisitRequest;
import com.kanapa4.medical_clinic_proxy.model.DoctorResponse;
import com.kanapa4.medical_clinic_proxy.model.Specialization;
import com.kanapa4.medical_clinic_proxy.model.VisitResponse;
import com.kanapa4.medical_clinic_proxy.service.ProxyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/proxy")
@RequiredArgsConstructor
public class ProxyController {
    private final ProxyService proxyService;

    @GetMapping("/patients/{patientId}/visits")
    public List<VisitResponse> getPatientVisits(@PathVariable Long patientId) {
        log.info("Received request to get visits for patient ID: {}", patientId);
        return proxyService.getPatientVisits(patientId);
    }

    @PatchMapping("/visits/{visitId}/book")
    public VisitResponse bookVisit(@PathVariable Long visitId, @RequestBody BookVisitRequest request) {
        log.info("Received request to book visit ID: {} for patient ID: {}", visitId, request.getPatientId());
        return proxyService.bookVisit(visitId, request.getPatientId());
    }

    @GetMapping("/visits/available/doctor/{doctorId}")
    public List<VisitResponse> getAvailableVisitsByDoctor(@PathVariable Long doctorId) {
        log.info("Received request to get available visits for doctor ID: {}", doctorId);
        return proxyService.getAvailableVisitsByDoctor(doctorId);
    }

    @GetMapping("/visits/available")
    public List<VisitResponse> getAvailableVisitsBySpecializationAndDate(
            @RequestParam Specialization specialization,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Received request to get available visits for specialization: {} on date: {}", specialization, date);
        return proxyService.getAvailableVisitsBySpecializationAndDate(specialization, date);
    }

    @GetMapping("/doctors/{doctorId}/visits")
    public List<VisitResponse> getDoctorVisits(@PathVariable Long doctorId) {
        log.info("Received request to get all visits for doctor ID: {}", doctorId);
        return proxyService.getDoctorVisits(doctorId);
    }

    @PatchMapping("/visits/{visitId}/cancel")
    public VisitResponse cancelVisit(@PathVariable Long visitId) {
        log.info("Received request to cancel visit ID: {}", visitId);
        return proxyService.cancelVisit(visitId);
    }

    @GetMapping("/visits/available-range")
    public List<VisitResponse> getAvailableVisitsByDateRange(
            @RequestParam(required = false) Specialization specialization,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("Received request to get available visits from {} to {}", startDate, endDate);
        return proxyService.getAvailableVisitsByDateRange(specialization, startDate, endDate);
    }

    @GetMapping("/visits/search")
    public List<VisitResponse> getVisitsBySpecializationAndDateRange(
            @RequestParam Specialization specialization,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("Received request to search visits for spec {} from {} to {}", specialization, startDate, endDate);
        return proxyService.getVisitsBySpecializationAndDateRange(specialization, startDate, endDate);
    }

    @GetMapping("/doctors")
    public List<DoctorResponse> getDoctorsBySpecialization(@RequestParam Specialization specialization) {
        log.info("Received request to get doctors for specialization: {}", specialization);
        return proxyService.getDoctorsBySpecialization(specialization);
    }
}
