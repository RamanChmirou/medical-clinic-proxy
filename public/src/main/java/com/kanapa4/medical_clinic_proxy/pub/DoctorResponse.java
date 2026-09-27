package com.kanapa4.medical_clinic_proxy.pub;

import com.kanapa4.medical_clinic_proxy.model.Specialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Specialization specialization;
}
