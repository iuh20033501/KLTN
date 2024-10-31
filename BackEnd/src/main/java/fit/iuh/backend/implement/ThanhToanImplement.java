package fit.iuh.backend.implement;

import fit.iuh.backend.enumclass.ThanhToanEnum;
import fit.iuh.backend.moudel.TaiLieu;
import fit.iuh.backend.moudel.ThanhToan;
import fit.iuh.backend.repository.ThanhToanRepo;
import fit.iuh.backend.service.ThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ThanhToanImplement implements ThanhToanService {
    @Autowired
    private ThanhToanRepo thanhToanRepo;

    @Override
    public ThanhToan createThanhToan(ThanhToan thanhToan) {
        return thanhToanRepo.save(thanhToan);
    }

    @Override
    public Optional<ThanhToan> findById(Long id) {
        return thanhToanRepo.findById(id);
    }

    @Override
    public List<ThanhToan> finfByIdLop(Long idLop) {
        return thanhToanRepo.findByIdLop(idLop);
    }

    @Override
    public List<ThanhToan> findByIdHV(Long idHV) {
        return thanhToanRepo.findByIdHV(idHV);
    }

    @Override
    public List<ThanhToan> findByIdHoaDon(Long idHD) {
        return thanhToanRepo.findByIdHD(idHD);
    }

    @Override
    public void deleteThanhToan(Long idTT) {
        thanhToanRepo.deleteById(idTT);
        return ;
    }

    @Override
    public List<ThanhToan> findAll() {
        return thanhToanRepo.findAll();
    }

    @Override
    public ThanhToan updateThanhToan(ThanhToan thanhToan) {
        return thanhToanRepo.save(thanhToan);
    }

    @Override
    public List<ThanhToan> findByIDHVvaEnum(Long idHV, ThanhToanEnum thanhToanEnum) {
        return thanhToanRepo.findByIdHVAndTrangThaiWait(idHV,thanhToanEnum);
    }
}