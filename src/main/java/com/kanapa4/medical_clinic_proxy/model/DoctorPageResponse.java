package com.kanapa4.medical_clinic_proxy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorPageResponse {
    private List<DoctorDto> content;
    private Integer totalPages;
    private Long totalElements;
    private Integer size;
    private Integer number;
}
