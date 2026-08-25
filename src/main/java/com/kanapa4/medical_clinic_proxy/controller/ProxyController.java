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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/proxy")
@RequiredArgsConstructor
@Tag(name = "Proxy Controller", description = "API for proxying requests to the Medical Clinic")
public class ProxyController {
    private final ProxyService proxyService;

    @Operation(summary = "Get patient visits", description = "Retrieves a list of all visits for a specific patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the visits",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VisitResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content)
    })
    @GetMapping("/patients/{patientId}/visits")
    public List<VisitResponse> getPatientVisits(@Parameter(description = "ID of the patient") @PathVariable Long patientId) {
        log.info("Received request to get visits for patient ID: {}", patientId);
        return proxyService.getPatientVisits(patientId);
    }

    @Operation(summary = "Book a visit", description = "Books an available visit for a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visit successfully booked",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VisitResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Visit or Patient not found", content = @Content)
    })
    @PatchMapping("/visits/{visitId}/book")
    public VisitResponse bookVisit(@Parameter(description = "ID of the visit to book") @PathVariable Long visitId, 
                                   @Parameter(description = "Booking details") @RequestBody BookVisitRequest request) {
        log.info("Received request to book visit ID: {} for patient ID: {}", visitId, request.getPatientId());
        return proxyService.bookVisit(visitId, request.getPatientId());
    }

    @Operation(summary = "Get available visits by doctor", description = "Retrieves a list of available visits for a specific doctor")
    @GetMapping("/visits/available/doctor/{doctorId}")
    public List<VisitResponse> getAvailableVisitsByDoctor(@Parameter(description = "ID of the doctor") @PathVariable Long doctorId) {
        log.info("Received request to get available visits for doctor ID: {}", doctorId);
        return proxyService.getAvailableVisitsByDoctor(doctorId);
    }

    @Operation(summary = "Get available visits by specialization and date", description = "Retrieves available visits for a specific specialization on a specific date")
    @GetMapping("/visits/available")
    public List<VisitResponse> getAvailableVisitsBySpecializationAndDate(
            @Parameter(description = "Specialization of the doctor") @RequestParam Specialization specialization,
            @Parameter(description = "Date of the visit (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Received request to get available visits for specialization: {} on date: {}", specialization, date);
        return proxyService.getAvailableVisitsBySpecializationAndDate(specialization, date);
    }

    @Operation(summary = "Get doctor visits", description = "Retrieves a list of all visits for a specific doctor")
    @GetMapping("/doctors/{doctorId}/visits")
    public List<VisitResponse> getDoctorVisits(@Parameter(description = "ID of the doctor") @PathVariable Long doctorId) {
        log.info("Received request to get all visits for doctor ID: {}", doctorId);
        return proxyService.getDoctorVisits(doctorId);
    }

    @Operation(summary = "Cancel a visit", description = "Cancels a booked visit")
    @PatchMapping("/visits/{visitId}/cancel")
    public VisitResponse cancelVisit(@Parameter(description = "ID of the visit to cancel") @PathVariable Long visitId) {
        log.info("Received request to cancel visit ID: {}", visitId);
        return proxyService.cancelVisit(visitId);
    }

    @Operation(summary = "Get available visits by date range", description = "Retrieves a list of available visits within a specific date range, optionally filtered by specialization")
    @GetMapping("/visits/available-range")
    public List<VisitResponse> getAvailableVisitsByDateRange(
            @Parameter(description = "Specialization of the doctor (optional)") @RequestParam(required = false) Specialization specialization,
            @Parameter(description = "Start date (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("Received request to get available visits from {} to {}", startDate, endDate);
        return proxyService.getAvailableVisitsByDateRange(specialization, startDate, endDate);
    }

    @Operation(summary = "Search visits", description = "Searches for visits by specialization and date range")
    @GetMapping("/visits/search")
    public List<VisitResponse> getVisitsBySpecializationAndDateRange(
            @Parameter(description = "Specialization of the doctor") @RequestParam Specialization specialization,
            @Parameter(description = "Start date (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("Received request to search visits for spec {} from {} to {}", specialization, startDate, endDate);
        return proxyService.getVisitsBySpecializationAndDateRange(specialization, startDate, endDate);
    }

    @Operation(summary = "Get doctors by specialization", description = "Retrieves a list of doctors with a specific specialization")
    @GetMapping("/doctors")
    public List<DoctorResponse> getDoctorsBySpecialization(@Parameter(description = "Specialization of the doctor") @RequestParam Specialization specialization) {
        log.info("Received request to get doctors for specialization: {}", specialization);
        return proxyService.getDoctorsBySpecialization(specialization);
    }
}
