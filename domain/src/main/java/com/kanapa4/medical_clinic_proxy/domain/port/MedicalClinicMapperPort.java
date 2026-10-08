package com.kanapa4.medical_clinic_proxy.domain.port;

import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import com.kanapa4.medical_clinic_proxy.pub.DoctorResponse;
import com.kanapa4.medical_clinic_proxy.pub.VisitResponse;
import java.util.List;

public interface MedicalClinicMapperPort {

    VisitResponse toVisitResponse(VisitDto dto);

    List<VisitResponse> toVisitResponses(List<VisitDto> dtos);

    DoctorResponse toDoctorResponse(DoctorDto dto);

    List<DoctorResponse> toDoctorResponses(List<DoctorDto> dtos);
}
