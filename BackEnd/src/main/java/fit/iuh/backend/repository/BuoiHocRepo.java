package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.BuoiHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuoiHocRepo extends JpaRepository<BuoiHoc,Long> {
    @Query("select bh from BuoiHoc bh join LopHoc  lop on bh.lopHoc.idLopHoc= lop.idLopHoc where bh.lopHoc.idLopHoc =:id")
    List<BuoiHoc> getBuoiTheoLop (@Param("id") Long id);
    @Query("SELECT bh FROM BuoiHoc bh " +
            "JOIN bh.lopHoc lop " +
            "JOIN HocVienLopHoc bhhv ON lop.idLopHoc = bhhv.key.lopHoc.idLopHoc " +
            "WHERE bhhv.key.hocVien.idUser =:id " +
            "ORDER BY bh.ngayHoc ASC")
    List<BuoiHoc> getBuoiHocTheoIdHocVien(@Param("id") Long id);

    @Query("select bh from BuoiHoc bh where bh.ngayHoc < CURRENT_DATE")
    List<BuoiHoc> getBuoiDaHoc();
    @Query("select bh from BuoiHoc bh where bh.ngayHoc < CURRENT_DATE AND bh.lopHoc.idLopHoc=:idLop")
    List<BuoiHoc> getBuoiDaHocTheoLop(@Param("idLop") Long id);

//    @Query("select bh from BuoiHoc bh where bh.ngayHoc < CURRENT_DATE AND bh.=:idHV")
//    List<BuoiHoc> getBuoiDaHocTheoLop(@Param("idHV") Long id);




}
