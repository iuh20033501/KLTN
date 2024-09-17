package com.example.backend.moudel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan {
    @Id
    private String tenDangNhap;
    @NotNull
    private String matKhau;
    private Boolean trangThai;
}
