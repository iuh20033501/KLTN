package fit.iuh.backend.service;

import fit.iuh.backend.moudel.GiangVien;
import fit.iuh.backend.moudel.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GiangVienService {
    Optional<GiangVien> findById(Long id);
    GiangVien createGiangVien (GiangVien giangVien);
    List<GiangVien> findAll();
    GiangVien findByName(String name);
    List<User> finDangLamViec();
}
