package com.kanapa4.medical_clinic_proxy.adapter.config;

import com.kanapa4.medical_clinic_proxy.domain.ProxyService;
import com.kanapa4.medical_clinic_proxy.domain.port.MedicalClinicClientPort;
import com.kanapa4.medical_clinic_proxy.domain.port.MedicalClinicMapperPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ProxyService proxyService(MedicalClinicClientPort clientPort, MedicalClinicMapperPort mapperPort) {
        return new ProxyService(clientPort, mapperPort);
    }
}
