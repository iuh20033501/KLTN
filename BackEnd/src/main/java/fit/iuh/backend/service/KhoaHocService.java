package fit.iuh.backend.service;

import fit.iuh.backend.moudel.KhoaHoc;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface KhoaHocService {
    Optional<KhoaHoc> findById(Long id);
    KhoaHoc createKhoaHoc(KhoaHoc khoaHoc);
    List<KhoaHoc> getAll();
    List<KhoaHoc> getAllTrue();
    List<KhoaHoc> getListKhoaYear(String year);
    List<KhoaHoc> getListKhoaLikeName(String name);
    List<KhoaHoc> getListKhoaActiveTrueLikeName(String name);

}
