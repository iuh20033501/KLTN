package fit.iuh.backend.implement;

import fit.iuh.backend.repository.KhoaHocRepo;
import fit.iuh.backend.service.KhoaHocService;
import fit.iuh.backend.moudel.KhoaHoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component
public class KhoaHocImplement implements KhoaHocService {
    @Autowired
    private KhoaHocRepo khoaHocRepo;

    @Override
    public Optional<KhoaHoc> findById(Long id) {
        return khoaHocRepo.findById(id);
    }

    @Override
    public KhoaHoc createKhoaHoc(KhoaHoc khoaHoc) {
        return khoaHocRepo.save(khoaHoc);
    }

    @Override
    public List<KhoaHoc> getAll() {
        return khoaHocRepo.findAll();
    }
}
