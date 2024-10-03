package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.HocVien;
import fit.iuh.backend.moudel.HocVienLopHoc;
import fit.iuh.backend.moudel.HocVienLopHocKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface HocVienLopHocRepo extends JpaRepository<HocVienLopHoc, HocVienLopHocKey> {
 @Query("select hv from HocVienLopHoc hv join hocvien h on  h.idUser= hv.key.HocVien.idUser  ")
  List<HocVien> getAllHocVienonLop(Long id);
}
