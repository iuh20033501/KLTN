package fit.iuh.backend.service;

import fit.iuh.backend.enumclass.ThanhToanEnum;
import fit.iuh.backend.moudel.TaiLieu;
import fit.iuh.backend.moudel.ThanhToan;
import fit.iuh.backend.repository.ThanhToanRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ThanhToanService {
    ThanhToan createThanhToan(ThanhToan thanhToan);
    Optional<ThanhToan> findById(Long id);
    List<ThanhToan> finfByIdLop(Long idLop);
    List<ThanhToan> findByIdHV(Long idHV);
    List<ThanhToan> findByIdHoaDon(Long idHD);
    void deleteThanhToan(Long idTT);
    List<ThanhToan> findAll();
    ThanhToan updateDoneThanhToanAndIdHoaDon (ThanhToan thanhToan, Long idHoaDon);
    ThanhToan updateCancelThanhToan(ThanhToan thanhToan);
    List<ThanhToan> findByIDHVvaEnum(Long idHV, ThanhToanEnum thanhToanEnum);
    List<ThanhToan> findByIdLopvaEnum(Long idLop, ThanhToanEnum thanhToanEnum);
    void reLoadThanhToanByIdLop(Long idLop);
    List<ThanhToan> findByIdHVAndHoaDonNUll(Long idHV);
}
