package com.example.backend.Repository;

import com.example.backend.moudel.HocVien;
import com.example.backend.moudel.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepo extends JpaRepository<NhanVien,Long> {
}
