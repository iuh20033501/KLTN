package fit.iuh.backend.service;

import fit.iuh.backend.moudel.HocVien;
import fit.iuh.backend.moudel.HocVienLopHoc;
import fit.iuh.backend.moudel.HocVienLopHocKey;
import fit.iuh.backend.moudel.LopHoc;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HocVienLopHocService {
    List<HocVienLopHoc> findByidLop(Long idLop);
    List<HocVienLopHoc> findByIdHocVien(Long idHV);
    HocVienLopHoc dangKyLopHoc(HocVienLopHocKey key);


}
