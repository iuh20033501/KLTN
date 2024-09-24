package com.example.backend.Repository;

import com.example.backend.moudel.HocVienLopHoc;
import com.example.backend.moudel.HocVienLopHocKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface HocVienLopHocRepo extends JpaRepository<HocVienLopHoc, HocVienLopHocKey> {
// @Query("se")
}
