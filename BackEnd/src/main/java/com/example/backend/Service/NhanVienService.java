package com.example.backend.Service;

import com.example.backend.moudel.NhanVien;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface NhanVienService {
    Optional<NhanVien> findById(Long id);
    NhanVien createNhanVien(NhanVien nhanVien);
}
