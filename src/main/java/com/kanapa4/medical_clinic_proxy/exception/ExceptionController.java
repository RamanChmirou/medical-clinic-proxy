package com.kanapa4.medical_clinic_proxy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MedicalClinicProxyException.class)
    public ResponseEntity<ProblemDetail> handleMedicalClinicProxyException(MedicalClinicProxyException exception) {
        HttpStatus status = exception.getHttpStatus();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                status,
                exception.getMessage()
        );
        return ResponseEntity.status(status).body(problemDetail);
    }
}
