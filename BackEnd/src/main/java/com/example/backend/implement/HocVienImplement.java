package com.example.backend.implement;

import com.example.backend.Repository.HocVienRepo;
import com.example.backend.Service.HocVienService;
import com.example.backend.moudel.HocVien;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class HocVienImplement implements HocVienService {
    @Autowired
    private HocVienRepo hocVienRepo;

    @Override
    public Optional<HocVien> findByIdHocVien(Long id) {
        return hocVienRepo.findById(id);
    }

    @Override
    public HocVien createHocVien(HocVien hocVien) {
        return hocVienRepo.save(hocVien);
    }

    @Override
    public HocVien findByName(String name) {
        return hocVienRepo.findByHoTen(name);
    }

    @Override
    public List<HocVien> getAll() {
        return hocVienRepo.findAll();
    }
}
