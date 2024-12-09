package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.HocVien;
import fit.iuh.backend.moudel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Repository
public interface HocVienRepo extends JpaRepository<HocVien,Long> {
    HocVien findByHoTen(String hoTen);


}
