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
 @Query("select hv from HocVienLopHoc hv join hv.key.hocVien h where h.idUser =:idUser")
 List<HocVienLopHoc> getAllLopByIdHV(@Param("idUser") Long idUser);

 @Query("select lop from HocVienLopHoc lop join lop.key.lopHoc lh where lh.idLopHoc =:idLop")
 List<HocVienLopHoc> getAllHocVienonLop(@Param("idLop") Long idLop);
}
