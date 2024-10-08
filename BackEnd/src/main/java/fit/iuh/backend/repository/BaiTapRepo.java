package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.BaiTap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public interface BaiTapRepo extends JpaRepository<BaiTap,Long> {
    @Query("select bt from BaiTap bt join HocVienBaiTap hv on bt.hocVien.idHocVien.idUser= hv.idHocVien.idUser where bt.hocVien.idHocVien.idUser =:id")
    List<BaiTap> FindByIdHV(@Param("id") Long idHV);
    @Query("select bt from BaiTap  bt join BuoiHoc bh on bt.buoiHoc.idBuoiHoc = bh.idBuoiHoc where bt.buoiHoc.idBuoiHoc= :id")
    List<BaiTap> FindByIdBH (@Param("id") Long id);
}
