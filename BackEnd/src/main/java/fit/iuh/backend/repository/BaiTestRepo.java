package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.BaiTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaiTestRepo extends JpaRepository<BaiTest,Long> {
}
