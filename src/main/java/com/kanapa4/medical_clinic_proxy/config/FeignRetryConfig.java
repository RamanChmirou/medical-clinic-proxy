package com.kanapa4.medical_clinic_proxy.config;

import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignRetryConfig {

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 3000, 3);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        ErrorDecoder defaultDecoder = new ErrorDecoder.Default();
        return (String methodKey, feign.Response response) -> {
            if (response.status() == 503 || response.status() == 500) {
                return new RetryableException(
                        response.status(),
                        "Service Unavailable - retrying",
                        response.request().httpMethod(),
                        (Long) null,
                        response.request()
                );
            }
            return defaultDecoder.decode(methodKey, response);
        };
    }
}
