package fit.iuh.backend.service;

import fit.iuh.backend.moudel.HocVien;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface HocVienService {
     HocVien findByIdHocVien(Long id);
     HocVien createHocVien (HocVien hocVien);
     HocVien findByName (String name);
     List<HocVien> getAll();
}
