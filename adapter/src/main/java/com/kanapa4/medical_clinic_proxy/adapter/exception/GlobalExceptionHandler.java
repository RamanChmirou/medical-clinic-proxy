package com.kanapa4.medical_clinic_proxy.adapter.exception;

import com.kanapa4.medical_clinic_proxy.model.exception.MedicalClinicProxyException;
import com.kanapa4.medical_clinic_proxy.model.exception.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MedicalClinicProxyException.class)
    public ResponseEntity<ProblemDetail> handleMedicalClinicProxyException(MedicalClinicProxyException ex) {
        HttpStatus status = switch (ex) {
            case ServiceUnavailableException e -> HttpStatus.SERVICE_UNAVAILABLE;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        return ResponseEntity.status(status).body(problemDetail);
    }
}
