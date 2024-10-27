package fit.iuh.backend.service;

import fit.iuh.backend.moudel.NhanVien;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface NhanVienService {
    Optional<NhanVien> findById(Long id);
    NhanVien createNhanVien(NhanVien nhanVien);
    List<NhanVien> findAlL();
    NhanVien findByName(String name);
}
