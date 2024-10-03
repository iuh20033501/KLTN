package fit.iuh.backend.service;

import fit.iuh.backend.moudel.LopHoc;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface LopHocService {
    Optional<LopHoc> findById (Long id);
    LopHoc createLopHoc(LopHoc lopHoc);
}
