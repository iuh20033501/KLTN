package com.example.backend.implement;


import com.example.backend.Repository.TaiKhoanRepo;
import com.example.backend.Service.TaiKhoanService;
import com.example.backend.moudel.TaiKhoan;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TaiKhoanImplement implements TaiKhoanService {
    @Autowired
    private TaiKhoanRepo taiKhoanRepo;
    @Override
    public TaiKhoan findByTenDangNhap(String tenDangNhap) {
        return taiKhoanRepo.findByTenDangNhap(tenDangNhap);
    }

    @Override
    public TaiKhoan createTaiKhoan(TaiKhoan taiKhoan) {
        return taiKhoanRepo.save(taiKhoan);
    }

    @Override
    public List<TaiKhoan> getAll() {
        return taiKhoanRepo.findAll();
    }

}
