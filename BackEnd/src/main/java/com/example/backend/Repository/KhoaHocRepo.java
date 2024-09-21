package com.example.backend.Repository;

import com.example.backend.moudel.KhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoaHocRepo extends JpaRepository<KhoaHoc,Long> {
}
