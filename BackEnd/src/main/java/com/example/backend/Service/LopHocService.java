package com.example.backend.Service;

import com.example.backend.moudel.LopHoc;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface LopHocService {
    Optional<LopHoc> findById (Long id);
    LopHoc createLopHoc(LopHoc lopHoc);
}
