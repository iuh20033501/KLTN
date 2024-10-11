package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.HocVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HocVienRepo extends JpaRepository<HocVien,Long> {
    HocVien findByHoTen(String hoTen);

}
