package fit.iuh.backend.implement;

import fit.iuh.backend.moudel.HocVien;
import fit.iuh.backend.moudel.HocVienLopHoc;
import fit.iuh.backend.moudel.HocVienLopHocKey;
import fit.iuh.backend.moudel.LopHoc;
import fit.iuh.backend.repository.HocVienLopHocRepo;
import fit.iuh.backend.repository.HocVienRepo;
import fit.iuh.backend.repository.LopHocRepo;
import fit.iuh.backend.service.HocVienLopHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HocVienLopHocImplement implements HocVienLopHocService {
    @Autowired
    private HocVienLopHocRepo hocVienLopHocRepository;

    @Autowired
    private HocVienRepo hocVienRepository;

    @Autowired
    private LopHocRepo lopHocRepository;

    @Override
    public List<HocVienLopHoc> findByidLop(Long idLop) {
        return hocVienLopHocRepository.getAllHocVienonLop(idLop);
    }

    @Override
    public List<HocVienLopHoc> findByIdHocVien(Long idHV) {
        return hocVienLopHocRepository.getAllLopByIdHV(idHV);
    }

    @Override
    public HocVienLopHoc dangKyLopHoc(Long idHV, Long idLop) {
        HocVien hocVien = hocVienRepository.findById(idHV)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học viên với ID: " + idHV));

        LopHoc lopHoc = lopHocRepository.findById(idLop)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học với ID: " + idLop));

        HocVienLopHocKey key = new HocVienLopHocKey(hocVien, lopHoc);

        if (hocVienLopHocRepository.existsById(key)) {
            throw new RuntimeException("Học viên đã đăng ký lớp học này.");
        }

        HocVienLopHoc hocVienLopHoc = new HocVienLopHoc(key, true);

        return hocVienLopHocRepository.save(hocVienLopHoc);
    }
}