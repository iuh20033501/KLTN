package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.TienTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TienTrinhRepo extends JpaRepository<TienTrinh,Long> {
    @Query("select tt from TienTrinh  tt join hocvien hv on tt.hocVien.idUser= hv.idUser where tt.hocVien.idUser=:id ")
    List<TienTrinh> getByIdHV (@Param("id") Long idHV);
    @Query("select tt from TienTrinh  tt join BaiTap bt on tt.baiTap.idBaiTap= bt.idBaiTap where tt.baiTap.idBaiTap=:id")
    List<TienTrinh> getByIdBT (@Param("id") Long idBT);
    @Query("select tt from TienTrinh tt where tt.hocVien.idUser =:idHV and tt.baiTap.idBaiTap =:idBT")
    TienTrinh getByIdHVAndIdBT(@Param("idHV") Long idHV, @Param("idBT") Long idBT);

    @Query("select tt from TienTrinh tt join tt.baiTap.buoiHoc.lopHoc lop where tt.hocVien.idUser =:idHocVien AND lop.idLopHoc =:idLop")
    List<TienTrinh> getByIdHocVienIdLop(@Param("idHocVien") Long idHV, @Param("idLop") Long idLop);
    @Query("select tt from TienTrinh tt join tt.baiTap bt join bt.buoiHoc bh where bh.idBuoiHoc = :id ")
    List<TienTrinh> getByIdBuoi(@Param("id") Long idBuoi);

}
