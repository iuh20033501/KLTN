package com.example.backend.Repository;

import com.example.backend.moudel.HocVien;
import com.example.backend.moudel.TienTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TienTrinhRepo extends JpaRepository<TienTrinh,Long> {
}
