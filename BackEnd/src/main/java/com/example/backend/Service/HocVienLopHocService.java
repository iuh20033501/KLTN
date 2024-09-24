package com.example.backend.Service;

import com.example.backend.moudel.HocVien;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HocVienLopHocService {
    List<HocVien> findByidLop(Long id);

}
