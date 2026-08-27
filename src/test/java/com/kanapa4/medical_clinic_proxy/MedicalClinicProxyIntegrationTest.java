package com.kanapa4.medical_clinic_proxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@EnableWireMock
public class MedicalClinicProxyIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getVisits_ApiAvailable_ReturnVisits() throws Exception {
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("patientId", equalTo("1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"content\":[{\"id\":1}],\"totalPages\":1,\"totalElements\":1,\"size\":20,\"number\":0}")));
        mockMvc.perform(MockMvcRequestBuilders.get("/proxy/visits").param("patientId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getVisits_ApiUnavailable_ReturnFallback() throws Exception {
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("patientId", equalTo("2"))
                .willReturn(aResponse().withStatus(500)));
        mockMvc.perform(MockMvcRequestBuilders.get("/proxy/visits").param("patientId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getVisits_RetryOnFailure_SucceedsEventually() throws Exception {
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("patientId", equalTo("3"))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs(com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED)
                .willReturn(aResponse().withStatus(500))
                .willSetStateTo("First Failure"));
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("patientId", equalTo("3"))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs("First Failure")
                .willReturn(aResponse().withStatus(500))
                .willSetStateTo("Second Failure"));
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("patientId", equalTo("3"))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs("Second Failure")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"content\":[{\"id\":3}],\"totalPages\":1,\"totalElements\":1,\"size\":20,\"number\":0}")));
        mockMvc.perform(MockMvcRequestBuilders.get("/proxy/visits").param("patientId", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3));
        verify(3, getRequestedFor(urlPathEqualTo("/visits")).withQueryParam("patientId", equalTo("3")));
    }

    @Test
    void getVisits_AllRetriesFail_ReturnFallback() throws Exception {
        stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("patientId", equalTo("4"))
                .willReturn(aResponse().withStatus(500)));
        mockMvc.perform(MockMvcRequestBuilders.get("/proxy/visits").param("patientId", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(3, getRequestedFor(urlPathEqualTo("/visits")).withQueryParam("patientId", equalTo("4")));
    }

    @Test
    void bookVisit_ApiAvailable_ReturnVisit() throws Exception {
        stubFor(patch(urlEqualTo("/visits/1/book/2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"patientId\": 2}")));
        mockMvc.perform(MockMvcRequestBuilders.patch("/proxy/visits/1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"patientId\": 2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(2));
    }

    @Test
    void bookVisit_ApiUnavailable_Return503() throws Exception {
        stubFor(patch(urlEqualTo("/visits/1/book/2"))
                .willReturn(aResponse().withStatus(503)));
        mockMvc.perform(MockMvcRequestBuilders.patch("/proxy/visits/1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"patientId\": 2}"))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void getDoctorsBySpecialization_ApiAvailable_ReturnDoctorsList() throws Exception {
        stubFor(get(urlEqualTo("/doctors/specialization/CARDIOLOGY"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 1, \"firstName\": \"John\"}]")));
        mockMvc.perform(MockMvcRequestBuilders.get("/proxy/doctors")
                .param("specialization", "CARDIOLOGY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void getDoctorsBySpecialization_ApiUnavailable_ReturnFallback() throws Exception {
        stubFor(get(urlEqualTo("/doctors/specialization/CARDIOLOGY"))
                .willReturn(aResponse().withStatus(500)));
        mockMvc.perform(MockMvcRequestBuilders.get("/proxy/doctors")
                .param("specialization", "CARDIOLOGY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
