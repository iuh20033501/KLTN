package fit.iuh.backend.implement;

import fit.iuh.backend.repository.BuoiHocRepo;
import fit.iuh.backend.service.BuoiHocService;
import fit.iuh.backend.moudel.BuoiHoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class BuoiHocImplement implements BuoiHocService {
    @Autowired
    private BuoiHocRepo buoiHocRepo;

    @Override
    public BuoiHoc findById(Long id) {
        return buoiHocRepo.findById(id).get();
    }

    @Override
    public BuoiHoc createBuoiHoc(BuoiHoc buoiHoc) {
        return buoiHocRepo.save(buoiHoc);
    }

    @Override
    public List<BuoiHoc> getByIdLop(Long id) {
        return buoiHocRepo.getBuoiTheoLop( id);
    }

    @Override
    public List<BuoiHoc> getAll() {
        return buoiHocRepo.findAll();
    }

    @Override
    public List<BuoiHoc> getBuoiByHocVien(Long id) {
        return buoiHocRepo.getBuoiHocTheoIdHocVien(id);
    }

    @Override
    public List<BuoiHoc> getBuoiDaHoc() {
        return buoiHocRepo.getBuoiDaHoc();
    }

    @Override
    public List<BuoiHoc> getBuoiDaHocTheoLop(Long idLop) {
        return buoiHocRepo.getBuoiDaHocTheoLop(idLop);
    }
}
