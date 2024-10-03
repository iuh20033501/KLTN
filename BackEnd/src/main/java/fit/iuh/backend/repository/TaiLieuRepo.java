package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.TaiLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaiLieuRepo extends JpaRepository<TaiLieu,Long> {

}
