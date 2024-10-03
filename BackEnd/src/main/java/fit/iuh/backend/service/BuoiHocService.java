package fit.iuh.backend.service;

import fit.iuh.backend.moudel.BuoiHoc;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BuoiHocService {
    Optional<BuoiHoc> findById(Long id);
    BuoiHoc createBuoiHoc(BuoiHoc buoiHoc);
}
