package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.BuoiHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuoiHocRepo extends JpaRepository<BuoiHoc,Long> {
}