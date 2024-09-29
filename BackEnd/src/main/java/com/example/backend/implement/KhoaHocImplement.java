package com.example.backend.implement;

import com.example.backend.Repository.KhoaHocRepo;
import com.example.backend.Service.KhoaHocService;
import com.example.backend.moudel.KhoaHoc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class KhoaHocImplement implements KhoaHocService {
    @Autowired
    private KhoaHocRepo khoaHocRepo;

    @Override
    public Optional<KhoaHoc> findById(Long id) {
        return khoaHocRepo.findById(id);
    }

    @Override
    public KhoaHoc createKhoaHoc(KhoaHoc khoaHoc) {
        return khoaHocRepo.save(khoaHoc);
    }

    @Override
    public List<KhoaHoc> getAll() {
        return khoaHocRepo.findAll();
    }
}
