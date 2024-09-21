package com.example.backend.Repository;

import com.example.backend.moudel.HocVien;
import com.example.backend.moudel.TaiLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaiLieuRepo extends JpaRepository<TaiLieu,Long> {
}
