package com.kanapa4.medical_clinic_proxy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MedicalClinicProxyException extends RuntimeException {
    private final HttpStatus httpStatus;

    public MedicalClinicProxyException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
