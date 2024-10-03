package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.GiangVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiangVienRepo extends JpaRepository<GiangVien,Long> {
}
