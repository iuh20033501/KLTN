package fit.iuh.backend.service;

import fit.iuh.backend.moudel.HocVien;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HocVienLopHocService {
    List<HocVien> findByidLop(Long id);

}
