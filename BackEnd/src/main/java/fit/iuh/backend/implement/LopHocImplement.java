package fit.iuh.backend.implement;

import fit.iuh.backend.moudel.LopHoc;
import fit.iuh.backend.repository.LopHocRepo;
import fit.iuh.backend.service.LopHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LopHocImplement implements LopHocService {
    @Autowired
    private LopHocRepo lopHocRepo;

    @Override
    public Optional<LopHoc> findById(Long id) {
        return lopHocRepo.findById(id);
    }

    @Override
    public LopHoc createLopHoc(LopHoc lopHoc) {
        return lopHocRepo.save(lopHoc);
    }

    @Override
    public List<LopHoc> findAll() {
        return lopHocRepo.findAll();
    }

    @Override
    public List<LopHoc> findByGiangVien(Long idGv) {
        return lopHocRepo.getListLopByGiaoVien(idGv);
    }

    @Override
    public List<LopHoc> findByKhoa(Long idKhoa) {
        return lopHocRepo.getListLopByKhoaHoc(idKhoa);
    }
}
