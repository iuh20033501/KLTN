package com.example.backend.dto;

import com.example.backend.moudel.HocVien;
import com.example.backend.moudel.TaiKhoan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupHocViendto {
    private HocVien hocVien;
    private TaiKhoan taiKhoan;
}
