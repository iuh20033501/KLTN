package fit.iuh.backend.service;

import fit.iuh.backend.moudel.CauHoi;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CauHoiService {
    CauHoi createCauHoi (CauHoi ch);
    CauHoi findById (Long id);
    List<CauHoi> findAll();
    List<CauHoi> findByIdBaiTap(Long id);
    List<CauHoi> findByIdBaiTest(Long id);
    List<CauHoi> findByIdBaiTestandTrangThaiTrue(Long id);
    List<CauHoi> findByIdBaiTapandTrangThaiTrue(Long id);
}
