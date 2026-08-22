package com.kanapa4.medical_clinic_proxy.exception;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends MedicalClinicProxyException {

    public ServiceUnavailableException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
