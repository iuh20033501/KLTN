package com.example.backend.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChucVuEnum {
    ADMIN (0),
    QUANLY(1);
    private final int ValueChucVu;
}
