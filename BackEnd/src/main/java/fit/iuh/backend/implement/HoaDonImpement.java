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
    public HoaDon deleteThanhToan(Long idTT) {
        HoaDon hoaDon = hoaDonRepo.findById(idTT).orElseThrow(() -> new RuntimeException("Hoa Don not found"));
        hoaDon.setTrangThai(true);
        return hoaDonRepo.save(hoaDon);
    }

    @Override
    public List<HoaDon> findAll() {
        return hoaDonRepo.findAll();
    }
}
