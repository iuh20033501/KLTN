package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.TienTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TienTrinhRepo extends JpaRepository<TienTrinh,Long> {
}
