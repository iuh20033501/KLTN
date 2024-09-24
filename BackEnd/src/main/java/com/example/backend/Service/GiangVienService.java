package com.example.backend.Service;

import com.example.backend.moudel.GiangVien;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface GiangVienService {
    Optional<GiangVien> findById(Long id);
    GiangVien createGiangVien (GiangVien giangVien);
}
