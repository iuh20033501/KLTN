package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.CauHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CauHoiRepo extends JpaRepository<CauHoi,Long> {
    @Query("select ch from CauHoi ch join BaiTap b on ch.baiTap.idBaiTap= b.idBaiTap where ch.baiTap.idBaiTap=:id")
    List<CauHoi>  findByIdBaiTap(@Param("id") Long ID);
    @Query("select ch from CauHoi ch join BaiTest b on ch.baiTest.idTest= b.idTest where ch.baiTest.idTest=:id")
    List<CauHoi>  findByIdBaiTest(@Param("id") Long ID);
}
