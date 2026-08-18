package com.kanapa4.medical_clinic_proxy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitResponse {
    private Long id;
    private LocalDateTime dateTime;
    private Integer durationInMinutes;
    private Long doctorId;
    private Long patientId;
    private Long facilityId;
}
