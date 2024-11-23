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
    @Query("select hd from HoaDon hd where hd.nguoiLap.idUser = :id")
    List<HoaDon> getByIdNV(@Param("id") Long idNV);

    @Query("select distinct hd from ThanhToan tt join tt.hoaDon hd where tt.nguoiThanhToan.idUser = :id")
    List<HoaDon> getByIdHV(@Param("id") Long idHV);

    @Query("select distinct hd from ThanhToan tt join tt.hoaDon hd where tt.lopHoc.idLopHoc = :id")
    List<HoaDon> getByIdLop(@Param("id") Long idLop);

}
