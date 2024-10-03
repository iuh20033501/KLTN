package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LopHocRepo extends JpaRepository<LopHoc,Long> {
}
