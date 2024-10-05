package fit.iuh.backend.implement;

import fit.iuh.backend.moudel.CauHoi;
import fit.iuh.backend.repository.CauHoiRepo;
import fit.iuh.backend.service.CauHoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CauHoiImplement implements CauHoiService {
    @Autowired
    private CauHoiRepo repo;
    @Override
    public CauHoi createCauHoi(CauHoi ch) {
        return repo.save(ch);
    }

    @Override
    public CauHoi findById(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public List<CauHoi> findAll() {
        return repo.findAll();
    }

    @Override
    public List<CauHoi> findByIdBaiTap(Long id) {
        return repo.findByIdBaiTap(id);
    }

    @Override
    public List<CauHoi> findByIdBaiTest(Long id) {
        return repo.findByIdBaiTest(id);
    }
}
