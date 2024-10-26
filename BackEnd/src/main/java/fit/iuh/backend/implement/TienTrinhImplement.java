package fit.iuh.backend.implement;

import fit.iuh.backend.moudel.TienTrinh;
import fit.iuh.backend.repository.TienTrinhRepo;
import fit.iuh.backend.service.TienTrinhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TienTrinhImplement implements TienTrinhService {
    @Autowired
    private TienTrinhRepo tienTrinhRepo;
    @Override
    public TienTrinh createTT(TienTrinh tt) {
        return tienTrinhRepo.save(tt);
    }

    @Override
    public TienTrinh findById(Long id) {
        return tienTrinhRepo.findById(id).get();
    }

    @Override
    public List<TienTrinh> finfAll() {
        return tienTrinhRepo.findAll();
    }

    @Override
    public List<TienTrinh> findByIdHv(Long idHV) {
        return tienTrinhRepo.getByIdHV(idHV);
    }

    @Override
    public List<TienTrinh> findByIdBatTap(Long idbt) {
        return tienTrinhRepo.getByIdBT(idbt);
    }

    @Override
    public TienTrinh findByIdHvIdBTap(Long idHV, Long idBt) {
        return tienTrinhRepo.getByIdHVAndIdBT(idHV,idBt);
    }
}
