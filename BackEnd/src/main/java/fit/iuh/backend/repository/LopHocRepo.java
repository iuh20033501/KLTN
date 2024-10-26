package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LopHocRepo extends JpaRepository<LopHoc, Long> {
    @Query("SELECT l FROM LopHoc l JOIN l.giangVien gv WHERE gv.idUser = :id")
    List<LopHoc> getListLopByGiaoVien(@Param("id") Long idGv);

    @Query("SELECT l FROM LopHoc l JOIN l.khoaHoc k WHERE k.idKhoaHoc = :id")
    List<LopHoc> getListLopByKhoaHoc(@Param("id") Long idKhoa);
}
