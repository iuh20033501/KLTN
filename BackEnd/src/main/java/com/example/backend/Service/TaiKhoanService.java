package com.example.backend.Service;

import com.example.backend.moudel.TaiKhoan;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaiKhoanService {
    TaiKhoan findByTenDangNhap(String tenDangNhap);
    TaiKhoan createTaiKhoan(TaiKhoan taiKhoan);
    List<TaiKhoan> getAll();
}
