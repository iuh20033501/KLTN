package fit.iuh.backend.service;

import fit.iuh.backend.moudel.LopHoc;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

@Service
public interface LopHocService {
    Optional<LopHoc> findById (Long id);
    LopHoc createLopHoc(LopHoc lopHoc);
    List<LopHoc> findAll();
    List<LopHoc> findAllTrue();
    List<LopHoc> findByGiangVien(Long idGv);
    List<LopHoc> findByKhoa(Long idKhoa);

    List<LopHoc> findLikeNameGiangVien(String idGv);
    List<LopHoc> findLikeNameKhoa(String idKhoa);
    List<LopHoc> findLikeName(String name);


}
