package fit.iuh.backend.implement;

import fit.iuh.backend.enumclass.TrangThaiThanhToan;
import fit.iuh.backend.moudel.ThanhToan;
import fit.iuh.backend.repository.ThanhToanRepo;
import fit.iuh.backend.service.ThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
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
    public ThanhToan updateDoneThanhToanAndIdHoaDon(ThanhToan thanhToan, Long idHoaDon) {
        thanhToan.setTrangThai(TrangThaiThanhToan.DONE);
        return thanhToanRepo.save(thanhToan);
    }
    @Override
    public ThanhToan updateCancelThanhToan(ThanhToan thanhToan) {
        thanhToan.setTrangThai(TrangThaiThanhToan.CANCEL);
        return thanhToanRepo.save(thanhToan);
    }
    @Override
    public List<ThanhToan> findByIDHVvaEnum(Long idHV, TrangThaiThanhToan trangThaiThanhToan) {
        return thanhToanRepo.findByIdHVAndTrangThai(idHV, trangThaiThanhToan);
    }

    @Override
    public List<ThanhToan> findByIdLopvaEnum(Long idLop, TrangThaiThanhToan trangThaiThanhToan) {

        return  thanhToanRepo.findByIdLopAndTrangThai(idLop, trangThaiThanhToan);

    }

    @Override
    public void reLoadThanhToanByIdLop(Long idLop) {
        List<ThanhToan> list= thanhToanRepo.findByIdLopAndTrangThai(idLop, TrangThaiThanhToan.WAIT);
        Date currentDate = new Date();
        list.removeIf(tt -> {
            if (tt.getLopHoc().getNgayBD().before(currentDate)) {
                tt.setTrangThai(TrangThaiThanhToan.CANCEL);
                return true; // Xóa tt khỏi danh sách
            }
            return false;
        });

    }

    @Override
    public List<ThanhToan> findByIdHVAndHoaDonNUll(Long idHV) {
        return thanhToanRepo.findByIdHVAndHoaDonNUll(idHV);
    }

    @Override
    public List<ThanhToan> findByIdLopva2Enum(Long idLop, TrangThaiThanhToan trangThaiThanhToan, TrangThaiThanhToan trangThaiThanhToan2) {
        return thanhToanRepo.findByIdLopAnd2TrangThai(idLop,trangThaiThanhToan,trangThaiThanhToan2);
    }
}
