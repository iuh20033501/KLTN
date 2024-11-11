package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.BaiTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaiTestRepo extends JpaRepository<BaiTest,Long> {
    @Query("select test from BaiTest  test join LopHoc  lop on test.lopHoc.idLopHoc= lop.idLopHoc where test.lopHoc.idLopHoc =:id")
    List<BaiTest> getListTestByIdLop (@Param("id") Long idLop);
    @Query("select test from BaiTest  test join LopHoc  lop on test.lopHoc.idLopHoc= lop.idLopHoc where test.lopHoc.idLopHoc =:id AND test.TrangThai= true")
    List<BaiTest> getListTestByIdLopandTrangThaitrue (@Param("id") Long idLop);
}
