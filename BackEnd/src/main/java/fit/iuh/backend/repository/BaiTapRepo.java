package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.BaiTap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaiTapRepo extends JpaRepository<BaiTap, Long> {
    @Query("SELECT bt FROM BaiTap bt JOIN bt.buoiHoc bh WHERE bh.idBuoiHoc =:id")
    List<BaiTap> findByIdBuoiHoc(@Param("id") Long id);
    @Query("SELECT bt FROM BaiTap bt JOIN bt.buoiHoc bh WHERE bh.idBuoiHoc =:id AND bt.trangThai= true")
    List<BaiTap> findByIdBuoiHocTrue(@Param("id") Long id);
}

