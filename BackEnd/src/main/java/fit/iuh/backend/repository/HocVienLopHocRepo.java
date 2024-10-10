package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.HocVien;
import fit.iuh.backend.moudel.HocVienLopHoc;
import fit.iuh.backend.moudel.HocVienLopHocKey;
import fit.iuh.backend.moudel.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface HocVienLopHocRepo extends JpaRepository<HocVienLopHoc, HocVienLopHocKey> {
 @Query("select hv from HocVienLopHoc hv join hocvien h on  h.idUser= hv.key.HocVien.idUser  where h.idUser =: id")
  List<HocVien> getAllLopByIdHV(@Param("id") Long id);
 @Query("select lop from HocVienLopHoc lop join LopHoc lh on  lh.idLopHoc= lop.key.LopHoc.idLopHoc where  lh.idLopHoc=: id ")
 List<LopHoc> getAllHocVienonLop( @Param("id") Long id);

}
