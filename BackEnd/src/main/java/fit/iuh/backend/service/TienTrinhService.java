package fit.iuh.backend.service;

import fit.iuh.backend.moudel.TienTrinh;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TienTrinhService {
    TienTrinh createTT(TienTrinh tt);
    TienTrinh findById(Long id);
    List<TienTrinh> finfAll();
    List<TienTrinh> findByIdHv(Long idHV);
    List<TienTrinh> findByIdBatTap(Long idbt);
}
