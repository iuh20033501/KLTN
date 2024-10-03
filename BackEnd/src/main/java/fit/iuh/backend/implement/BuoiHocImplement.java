package fit.iuh.backend.implement;

import fit.iuh.backend.repository.BuoiHocRepo;
import fit.iuh.backend.service.BuoiHocService;
import fit.iuh.backend.moudel.BuoiHoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class BuoiHocImplement implements BuoiHocService {
    @Autowired
    private BuoiHocRepo buoiHocRepo;

    @Override
    public Optional<BuoiHoc> findById(Long id) {
        return buoiHocRepo.findById(id);
    }

    @Override
    public BuoiHoc createBuoiHoc(BuoiHoc buoiHoc) {
        return buoiHocRepo.save(buoiHoc);
    }
}
