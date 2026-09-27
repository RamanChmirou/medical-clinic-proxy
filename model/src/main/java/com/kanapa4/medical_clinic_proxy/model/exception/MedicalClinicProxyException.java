package com.kanapa4.medical_clinic_proxy.model.exception;

public class MedicalClinicProxyException extends RuntimeException {

    public MedicalClinicProxyException(String message) {
        super(message);
    }

    public MedicalClinicProxyException(String message, Throwable cause) {
        super(message, cause);
    }
}
