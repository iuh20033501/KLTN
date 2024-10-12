package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.GiangVien;
import fit.iuh.backend.moudel.HocVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiangVienRepo extends JpaRepository<GiangVien,Long> {
    GiangVien findByHoTen(String hoTen);

}
