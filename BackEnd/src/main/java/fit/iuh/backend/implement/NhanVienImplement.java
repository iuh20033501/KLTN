package fit.iuh.backend.implement;

import fit.iuh.backend.repository.NhanVienRepo;
import fit.iuh.backend.service.NhanVienService;
import fit.iuh.backend.moudel.NhanVien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class NhanVienImplement implements NhanVienService {
    @Autowired
    private NhanVienRepo nhanVienRepo;

    @Override
    public Optional<NhanVien> findById(Long id) {
        return nhanVienRepo.findById(id);
    }

    @Override
    public NhanVien createNhanVien(NhanVien nhanVien) {
        return nhanVienRepo.save(nhanVien);
    }
}
