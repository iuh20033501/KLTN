package fit.iuh.backend.implement;

import fit.iuh.backend.moudel.TaiLieu;
import fit.iuh.backend.repository.TaiLieuRepo;
import fit.iuh.backend.service.TaiLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaiLieuImplement implements TaiLieuService {
    @Autowired
    TaiLieuRepo taiLieuRepo;
    @Override
    public TaiLieu createTaiLieu(TaiLieu tailieu) {
        return taiLieuRepo.save(tailieu);
    }

    @Override
    public Optional<TaiLieu> findById(Long id) {
        return taiLieuRepo.findById(id);
    }

    @Override
    public List<TaiLieu> finfByIdBuoi(Long idBuoi) {
        return taiLieuRepo.findByBuoiHoc(idBuoi);
    }

    @Override
    public List<TaiLieu> finfByIdLop(Long idLop) {
        return taiLieuRepo.findByLopHoc(idLop);
    }

    @Override
    public List<TaiLieu> finfAll() {
        return taiLieuRepo.findAll();
    }


}
