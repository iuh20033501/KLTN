package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.KhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoaHocRepo extends JpaRepository<KhoaHoc,Long> {
}
