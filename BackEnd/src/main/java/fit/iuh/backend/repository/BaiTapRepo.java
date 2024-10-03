package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.BaiTap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

@Controller
public interface BaiTapRepo extends JpaRepository<BaiTap,Long> {
}
