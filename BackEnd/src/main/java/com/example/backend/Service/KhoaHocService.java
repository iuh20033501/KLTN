package com.example.backend.Service;

import com.example.backend.moudel.KhoaHoc;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface KhoaHocService {
    Optional<KhoaHoc> findById(Long id);
    KhoaHoc createKhoaHoc(KhoaHoc khoaHoc);
    List<KhoaHoc> getAll();
}
