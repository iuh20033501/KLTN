package com.example.backend.Repository;

import com.example.backend.moudel.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface TaiKhoanRepo extends JpaRepository <TaiKhoan,String>{
    TaiKhoan findByTenDangNhap(String tenDangNhap);
}
