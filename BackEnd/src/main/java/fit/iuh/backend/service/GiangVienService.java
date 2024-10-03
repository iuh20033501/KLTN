package fit.iuh.backend.service;

import fit.iuh.backend.moudel.GiangVien;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface GiangVienService {
    Optional<GiangVien> findById(Long id);
    GiangVien createGiangVien (GiangVien giangVien);
}
