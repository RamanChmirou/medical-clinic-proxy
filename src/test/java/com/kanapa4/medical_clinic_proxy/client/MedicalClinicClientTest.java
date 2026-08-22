package com.kanapa4.medical_clinic_proxy.client;

import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock
public class MedicalClinicClientTest {
    @Autowired
    private MedicalClinicClient medicalClinicClient;

    @Test
    void getPatientVisits_WireMockConfigured_ReturnVisitList() {
        stubFor(get(urlEqualTo("/visits/patient/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 1, \"durationInMinutes\": 30}]")));
        List<VisitDto> visits = medicalClinicClient.getPatientVisits(1L);
        assertNotNull(visits);
        assertFalse(visits.isEmpty());
    }
}
