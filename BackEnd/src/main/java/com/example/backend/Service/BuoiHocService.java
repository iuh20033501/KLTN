package com.example.backend.Service;

import com.example.backend.moudel.BuoiHoc;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BuoiHocService {
    Optional<BuoiHoc> findById(Long id);
    BuoiHoc createBuoiHoc(BuoiHoc buoiHoc);
}
