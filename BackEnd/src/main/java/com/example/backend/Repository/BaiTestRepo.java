package com.example.backend.Repository;

import com.example.backend.moudel.BaiTest;
import com.example.backend.moudel.HocVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaiTestRepo extends JpaRepository<BaiTest,Long> {
}
