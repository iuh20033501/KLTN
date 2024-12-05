package fit.iuh.backend.service;

import fit.iuh.backend.moudel.HoaDon;
import fit.iuh.backend.moudel.ThanhToan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface HoaDonService {
    HoaDon createHoaDon(HoaDon hoaDon);
    Optional<HoaDon> findById(Long id);
    List<HoaDon> finfByIdNhanVien(Long idNV);
    List<HoaDon> finfByIdHocVien(Long idHV);
    List<HoaDon> finfByIdLop(Long idLop);
//    HoaDon deleteHoaDon(Long idHD);
    List<HoaDon> findAll();
    List<Object[]> tongTienHoaDonTheoNam();
    List<HoaDon> getHoaDonByYear(int nam);
    List<HoaDon> getHoaDonLikeNameNV(String name);
}
