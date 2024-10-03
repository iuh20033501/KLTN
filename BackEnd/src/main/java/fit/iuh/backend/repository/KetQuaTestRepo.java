package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.KetQuaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KetQuaTestRepo extends JpaRepository<KetQuaTest,Long> {
}
