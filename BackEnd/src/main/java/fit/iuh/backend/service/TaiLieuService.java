package fit.iuh.backend.service;

import fit.iuh.backend.moudel.TaiLieu;
import fit.iuh.backend.moudel.TienTrinh;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TaiLieuService {
    TaiLieu createTaiLieu(TaiLieu tailieu);
    Optional<TaiLieu> findById(Long id);
    List<TaiLieu> finfByIdBuoi(Long idBuoi);
    List<TaiLieu> finfByIdLop(Long idLop);
    List<TaiLieu> finfAll();
    //    List<TienTrinh> findByIdHv(Long idHV);

}
