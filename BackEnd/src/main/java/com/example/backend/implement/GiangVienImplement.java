package com.example.backend.implement;

import com.example.backend.Repository.GiangVienRepo;
import com.example.backend.Service.GiangVienService;
import com.example.backend.moudel.GiangVien;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class GiangVienImplement implements GiangVienService {
    @Autowired
    private GiangVienRepo giangVienRepo;

    @Override
    public Optional<GiangVien> findById(Long id) {
        return giangVienRepo.findById(id);
    }

    @Override
    public GiangVien createGiangVien(GiangVien giangVien) {
        return giangVienRepo.save(giangVien);
    }
}
