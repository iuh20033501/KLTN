package com.example.backend.dto;

import com.example.backend.moudel.NhanVien;
import com.example.backend.moudel.TaiKhoan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupNhanViendto {
    private NhanVien nhanVien;
    private TaiKhoan taiKhoan;
}
