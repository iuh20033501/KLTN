package fit.iuh.backend.service;

import fit.iuh.backend.moudel.BuoiHoc;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BuoiHocService {
    BuoiHoc findById(Long id);
    BuoiHoc createBuoiHoc(BuoiHoc buoiHoc);
    List<BuoiHoc> getByIdLop(Long id);
    List<BuoiHoc> getAll();
    List<BuoiHoc> getBuoiByHocVien(Long id);
    List<BuoiHoc> getBuoiDaHoc();
    List<BuoiHoc> getBuoiDaHocTheoLop(Long idLop);
}
