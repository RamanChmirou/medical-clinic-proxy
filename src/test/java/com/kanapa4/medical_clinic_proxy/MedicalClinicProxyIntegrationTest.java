package com.kanapa4.medical_clinic_proxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;
import org.springframework.test.web.servlet.MockMvc;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@EnableWireMock
public class MedicalClinicProxyIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPatientVisits_ApiAvailable_ReturnVisits() throws Exception {
        stubFor(get(urlEqualTo("/visits/patient/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 1}]")));
        mockMvc.perform(MockMvcRequestBuilders.get("/proxy/patients/1/visits"))
                .andExpect(status().isOk());
    }

    @Test
    void getPatientVisits_ApiUnavailable_ReturnFallback() throws Exception {
        stubFor(get(urlEqualTo("/visits/patient/2"))
                .willReturn(aResponse().withStatus(500)));
        mockMvc.perform(MockMvcRequestBuilders.get("/proxy/patients/2/visits"))
                .andExpect(status().isOk());
    }
}
