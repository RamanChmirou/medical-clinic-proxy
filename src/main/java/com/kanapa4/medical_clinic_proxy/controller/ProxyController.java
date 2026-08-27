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

    @Operation(summary = "Search visits", description = "Searches for visits based on various optional filters like doctor, specialization, date range, and availability status")
    @GetMapping("/visits")
    public List<VisitResponse> getVisits(
            @Parameter(description = "ID of the patient") @RequestParam(required = false) Long patientId,
            @Parameter(description = "ID of the doctor") @RequestParam(required = false) Long doctorId,
            @Parameter(description = "Specialization of the doctor") @RequestParam(required = false) Specialization specialization,
            @Parameter(description = "Start date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Filter only available visits") @RequestParam(required = false, defaultValue = "false") boolean availableOnly) {
        
        log.info("Received request to get visits with filters: patientId={}, doctorId={}, specialization={}, startDate={}, endDate={}, availableOnly={}", 
                 patientId, doctorId, specialization, startDate, endDate, availableOnly);
                 
        return proxyService.getVisits(patientId, doctorId, specialization, startDate, endDate, availableOnly);
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

    @Operation(summary = "Cancel a visit", description = "Cancels a booked visit")
    @PatchMapping("/visits/{visitId}/cancel")
    public VisitResponse cancelVisit(@Parameter(description = "ID of the visit to cancel") @PathVariable Long visitId) {
        log.info("Received request to cancel visit ID: {}", visitId);
        return proxyService.cancelVisit(visitId);
    }

    @Operation(summary = "Get doctors by specialization", description = "Retrieves a list of doctors with a specific specialization")
    @GetMapping("/doctors")
    public List<DoctorResponse> getDoctorsBySpecialization(@Parameter(description = "Specialization of the doctor") @RequestParam Specialization specialization) {
        log.info("Received request to get doctors for specialization: {}", specialization);
        return proxyService.getDoctorsBySpecialization(specialization);
    }
}
