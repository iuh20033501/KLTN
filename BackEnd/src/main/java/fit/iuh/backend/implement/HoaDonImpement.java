package fit.iuh.backend.implement;

import fit.iuh.backend.moudel.HoaDon;
import fit.iuh.backend.moudel.ThanhToan;
import fit.iuh.backend.repository.HoaDonRepo;
import fit.iuh.backend.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HoaDonImpement implements HoaDonService {
    @Autowired
    private  HoaDonRepo hoaDonRepo;

    @Override
    public HoaDon createHoaDon(HoaDon hoaDon) {
        return hoaDonRepo.save(hoaDon);
    }

    @Override
    public Optional<HoaDon> findById(Long id) {
        return hoaDonRepo.findById(id);
    }

    @Override
    public List<HoaDon> finfByIdNhanVien(Long idNV) {
        return hoaDonRepo.getByIdNV(idNV);
    }

    @Override
    public List<HoaDon> finfByIdHocVien(Long idHV) {
        return hoaDonRepo.getByIdHV(idHV);
    }

    @Override
    public List<HoaDon> finfByIdLop(Long idLop) {
        return hoaDonRepo.getByIdLop(idLop);
    }

//    @Override
//    public HoaDon deleteHoaDon(Long idHD) {
//        HoaDon hoaDon = hoaDonRepo.findById(idHD).orElseThrow(() -> new RuntimeException("Hoa Don not found"));
//        hoaDon.setTrangThai(false);
//        return hoaDonRepo.save(hoaDon);
//    }

    @Override
    public List<HoaDon> findAll() {
        return hoaDonRepo.findAll();
    }

    @Override
    public List<Object[]> tongTienHoaDonTheoNam() {
        return hoaDonRepo.tongTienHoaDonTheoNam();
    }

    @Override
    public List<HoaDon> getHoaDonByYear(int nam) {
        return hoaDonRepo.findHoaDonByYear(nam);
    }

    @Override
    public List<HoaDon> getHoaDonLikeNameNV(String name) {
        return hoaDonRepo.getListLikeTen(name);
    }
}
