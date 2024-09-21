package com.example.backend.Repository;

import com.example.backend.moudel.BaiTap;
import com.example.backend.moudel.HocVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

@Controller
public interface BaiTapRepo extends JpaRepository<BaiTap,Long> {
}
