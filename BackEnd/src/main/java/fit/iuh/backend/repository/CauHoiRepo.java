package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.CauHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CauHoiRepo extends JpaRepository<CauHoi,Long> {
}
