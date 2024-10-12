package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.ThanhToan;
import fit.iuh.backend.moudel.TienTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface ThanhToanRepo extends JpaRepository<ThanhToan,Long> {
    @Query("select tt from ThanhToan  tt join hocvien hv on tt.nguoiThanhToan.idUser= hv.idUser where tt.nguoiThanhToan.idUser= :id")
    List<ThanhToan> getByIdHV (@Param("id") Long idHV);
    @Query("select tt from ThanhToan  tt join LopHoc lop on tt.lopHoc.idLopHoc= lop.idLopHoc where tt.lopHoc.idLopHoc= :id")
    List<ThanhToan> getByIdLop (@Param("id") Long idLop);
    @Query("select tt from ThanhToan  tt join HoaDon hd on tt.hoaDon.idHoaDon= hd.idHoaDon where tt.hoaDon.idHoaDon= :id")
    List<ThanhToan> getByIdHD (@Param("id") Long idHD);
}
