package fit.iuh.backend.implement;

import fit.iuh.backend.repository.GiangVienRepo;
import fit.iuh.backend.service.GiangVienService;
import fit.iuh.backend.moudel.GiangVien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class GiangVienImplement implements GiangVienService {
    @Autowired
    private GiangVienRepo giangVienRepo;

    @Override
    public Optional<GiangVien> findById(Long id) {
        return giangVienRepo.findById(id);
    }

    @Override
    public GiangVien createGiangVien(GiangVien giangVien) {
        return giangVienRepo.save(giangVien);
    }
}
