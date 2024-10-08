package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LopHocRepo extends JpaRepository<LopHoc,Long> {
    @Query("select l from LopHoc  l join GiangVien gv on l.giangVien.idUser= gv.idUser where l.giangVien.idUser= :id")
    List<LopHoc> getListLopByGiaoVien(@Param("id") Long idGv);
    @Query("select l from LopHoc  l join KhoaHoc k on l.khoaHoc.idKhoaHoc= k.idKhoaHoc where l.khoaHoc.idKhoaHoc= :id")
    List<LopHoc> getListLopByKhoaHoc(@Param("id") Long idKhoa);
}
