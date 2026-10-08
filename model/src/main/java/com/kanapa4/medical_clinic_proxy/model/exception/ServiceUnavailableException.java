package com.kanapa4.medical_clinic_proxy.model.exception;

public class ServiceUnavailableException extends MedicalClinicProxyException {

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
