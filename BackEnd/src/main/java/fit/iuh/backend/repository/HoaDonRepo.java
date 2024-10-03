package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepo extends JpaRepository<HoaDon,Long> {
}
