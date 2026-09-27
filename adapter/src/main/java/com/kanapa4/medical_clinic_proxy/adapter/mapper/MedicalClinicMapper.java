package com.kanapa4.medical_clinic_proxy.adapter.mapper;

import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import com.kanapa4.medical_clinic_proxy.pub.DoctorResponse;
import com.kanapa4.medical_clinic_proxy.pub.VisitResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicalClinicMapper {

    VisitResponse toVisitResponse(VisitDto dto);

    List<VisitResponse> toVisitResponses(List<VisitDto> dtos);

    DoctorResponse toDoctorResponse(DoctorDto dto);

    List<DoctorResponse> toDoctorResponses(List<DoctorDto> dtos);
}
