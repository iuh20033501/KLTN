package com.example.backend.implement;

import com.example.backend.Repository.BuoiHocRepo;
import com.example.backend.Service.BuoiHocService;
import com.example.backend.Service.HocVienService;
import com.example.backend.moudel.BuoiHoc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class BuoiHocImplement implements BuoiHocService {
    @Autowired
    private BuoiHocRepo buoiHocRepo;

    @Override
    public Optional<BuoiHoc> findById(Long id) {
        return buoiHocRepo.findById(id);
    }

    @Override
    public BuoiHoc createBuoiHoc(BuoiHoc buoiHoc) {
        return buoiHocRepo.save(buoiHoc);
    }
}
