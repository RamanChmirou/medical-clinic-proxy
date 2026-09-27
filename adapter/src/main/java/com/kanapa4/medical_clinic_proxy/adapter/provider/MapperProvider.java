package com.kanapa4.medical_clinic_proxy.adapter.provider;

import com.kanapa4.medical_clinic_proxy.adapter.mapper.MedicalClinicMapper;
import com.kanapa4.medical_clinic_proxy.domain.port.MedicalClinicMapperPort;
import com.kanapa4.medical_clinic_proxy.model.DoctorDto;
import com.kanapa4.medical_clinic_proxy.model.VisitDto;
import com.kanapa4.medical_clinic_proxy.pub.DoctorResponse;
import com.kanapa4.medical_clinic_proxy.pub.VisitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MapperProvider implements MedicalClinicMapperPort {

    private final MedicalClinicMapper mapper;

    @Override
    public VisitResponse toVisitResponse(VisitDto dto) {
        return mapper.toVisitResponse(dto);
    }

    @Override
    public List<VisitResponse> toVisitResponses(List<VisitDto> dtos) {
        return mapper.toVisitResponses(dtos);
    }

    @Override
    public DoctorResponse toDoctorResponse(DoctorDto dto) {
        return mapper.toDoctorResponse(dto);
    }

    @Override
    public List<DoctorResponse> toDoctorResponses(List<DoctorDto> dtos) {
        return mapper.toDoctorResponses(dtos);
    }
}
