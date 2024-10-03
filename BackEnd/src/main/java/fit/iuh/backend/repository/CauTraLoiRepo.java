package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.CauTraLoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CauTraLoiRepo extends JpaRepository<CauTraLoi,Long> {
}
