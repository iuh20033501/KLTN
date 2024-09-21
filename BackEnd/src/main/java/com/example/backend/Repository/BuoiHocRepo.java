package com.example.backend.Repository;

import com.example.backend.moudel.BuoiHoc;
import com.example.backend.moudel.HocVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuoiHocRepo extends JpaRepository<BuoiHoc,Long> {
}
