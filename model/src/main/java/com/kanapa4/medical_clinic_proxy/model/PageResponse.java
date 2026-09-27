package com.kanapa4.medical_clinic_proxy.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;

public record PageResponse<T>(
    List<T> content,
    @JsonAlias({"number", "page"})
    int page,
    int size,
    long totalElements,
    int totalPages
) {}
