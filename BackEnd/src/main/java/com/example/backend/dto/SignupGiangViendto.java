package com.example.backend.dto;

import com.example.backend.moudel.GiangVien;
import com.example.backend.moudel.TaiKhoan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupGiangViendto {
    private GiangVien giangVien;
    private TaiKhoan taiKhoan;
}
