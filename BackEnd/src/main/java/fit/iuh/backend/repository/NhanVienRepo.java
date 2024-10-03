package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepo extends JpaRepository<NhanVien,Long> {
}
