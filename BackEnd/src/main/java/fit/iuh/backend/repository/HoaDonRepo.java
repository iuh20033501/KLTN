package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.HoaDon;
import fit.iuh.backend.moudel.TienTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepo extends JpaRepository<HoaDon,Long> {
    @Query("select hd from HoaDon hd join NhanVien nv on hd.nguoiLap.idUser= nv.idUser where hd.nguoiLap.idUser=:id")
    List<HoaDon> getByIdNV (@Param("id") Long idNV);
    @Query("select hd from ThanhToan tt join HoaDon hd on tt.hoaDon.idHoaDon= hd.idHoaDon where tt.nguoiThanhToan.idUser=:id")
    List<HoaDon> getByIdHV (@Param("id") Long idHV);

    @Query("select hd from ThanhToan tt join HoaDon hd on tt.hoaDon.idHoaDon= hd.idHoaDon where tt.lopHoc.idLopHoc=:id")
    List<HoaDon> getByIdLop (@Param("id") Long idLop);
}
