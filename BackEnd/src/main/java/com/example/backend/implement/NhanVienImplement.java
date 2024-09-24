package com.example.backend.implement;

import com.example.backend.Repository.NhanVienRepo;
import com.example.backend.Service.NhanVienService;
import com.example.backend.moudel.NhanVien;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class NhanVienImplement implements NhanVienService {
    @Autowired
    private NhanVienRepo nhanVienRepo;

    @Override
    public Optional<NhanVien> findById(Long id) {
        return nhanVienRepo.findById(id);
    }

    @Override
    public NhanVien createNhanVien(NhanVien nhanVien) {
        return nhanVienRepo.save(nhanVien);
    }
}
