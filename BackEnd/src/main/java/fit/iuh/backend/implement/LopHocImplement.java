package fit.iuh.backend.implement;

import fit.iuh.backend.enumclass.TrangThaiLop;
import fit.iuh.backend.moudel.LopHoc;
import fit.iuh.backend.repository.LopHocRepo;
import fit.iuh.backend.service.LopHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class LopHocImplement implements LopHocService {
    @Autowired
    private LopHocRepo lopHocRepo;

    @Override
    public Optional<LopHoc> findById(Long id) {
        return lopHocRepo.findById(id);
    }

    @Override
    public LopHoc createLopHoc(LopHoc lopHoc) {
//        lopHoc.setTrangThai(TrangThaiLop.READY);
        return lopHocRepo.save(lopHoc);
    }

    @Override
    public List<LopHoc> findAll() {
        return lopHocRepo.findAll();
    }

    @Override
    public List<LopHoc> findAllTrue() {
        return lopHocRepo.getListByTrangThaiInList(Arrays.asList(TrangThaiLop.READY, TrangThaiLop.FULL));
    }



    @Override
    public List<LopHoc> findByGiangVien(Long idGv) {
        return lopHocRepo.getListLopByGiaoVien(idGv);
    }

    @Override
    public List<LopHoc> findByKhoa(Long idKhoa) {
        return lopHocRepo.getListLopByKhoaHoc(idKhoa);
    }

    @Override
    public List<LopHoc> findLikeNameGiangVien(String nameGV) {
        return lopHocRepo.getListLopLikenameGiaoVien(nameGV);
    }

    @Override
    public List<LopHoc> findLikeNameKhoa(String nameKhoa) {
        return lopHocRepo.getListLopLikenameKhoaHoc(nameKhoa);
    }


    @Override
    public List<LopHoc> findLikeName(String name) {
        return lopHocRepo.getListLikeTen(name);
    }


}
