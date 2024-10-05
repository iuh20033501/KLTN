package fit.iuh.backend.implement;

import fit.iuh.backend.repository.HocVienRepo;
import fit.iuh.backend.service.HocVienService;
import fit.iuh.backend.moudel.HocVien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class HocVienImplement implements HocVienService {
    @Autowired
    private HocVienRepo hocVienRepo;

    @Override
    public HocVien findByIdHocVien(Long id) {
        return hocVienRepo.findById(id).get();
    }

    @Override
    public HocVien createHocVien(HocVien hocVien) {
        return hocVienRepo.save(hocVien);
    }

    @Override
    public HocVien findByName(String name) {
        return hocVienRepo.findByHoTen(name);
    }

    @Override
    public List<HocVien> getAll() {
        return hocVienRepo.findAll();
    }
}
