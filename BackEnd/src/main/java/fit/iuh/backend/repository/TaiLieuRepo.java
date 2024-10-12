package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.TaiLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaiLieuRepo extends JpaRepository<TaiLieu,Long> {
    @Query("select tl from TaiLieu tl join BuoiHoc bh on tl.buoiHoc.idBuoiHoc= bh.idBuoiHoc where tl.buoiHoc.idBuoiHoc=:id")
    List<TaiLieu> findByBuoiHoc(@Param("id") Long idBuoiHoc);
}
