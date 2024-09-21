package com.example.backend.Repository;

import com.example.backend.moudel.CauTraLoi;
import com.example.backend.moudel.HocVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CauTraLoiRepo extends JpaRepository<CauTraLoi,Long> {
}
