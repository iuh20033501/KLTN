package fit.iuh.backend.implement;

import fit.iuh.backend.enumclass.ChucVu;
import fit.iuh.backend.moudel.User;
import fit.iuh.backend.repository.GiangVienRepo;
import fit.iuh.backend.repository.TaiKhoanRepo;
import fit.iuh.backend.service.GiangVienService;
import fit.iuh.backend.moudel.GiangVien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class GiangVienImplement implements GiangVienService {
    @Autowired
    private GiangVienRepo giangVienRepo;
    @Autowired
    private TaiKhoanRepo taiKhoanRepo;

    @Override
    public Optional<GiangVien> findById(Long id) {
        return giangVienRepo.findById(id);
    }

    @Override
    public GiangVien createGiangVien(GiangVien giangVien) {
        return giangVienRepo.save(giangVien);
    }

    @Override
    public List<GiangVien> findAll() {
        return giangVienRepo.findAll();
    }

    @Override
    public GiangVien findByName(String name) {
        return giangVienRepo.findByHoTen(name);
    }

    @Override
    public List<User> finDangLamViec() {
        return taiKhoanRepo.findUserByTaiKhoanEnableTrue(ChucVu.TEACHER);
    }
}
