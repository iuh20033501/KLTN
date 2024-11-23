package fit.iuh.backend.repository;

import fit.iuh.backend.enumclass.TrangThaiThanhToan;
import fit.iuh.backend.moudel.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhToanRepo extends JpaRepository<ThanhToan, Long> {

    @Query("SELECT tt FROM ThanhToan tt JOIN tt.nguoiThanhToan hv ON tt.nguoiThanhToan.idUser = hv.idUser WHERE tt.nguoiThanhToan.idUser =:id")
    List<ThanhToan> findByIdHV(@Param("id") Long idHV);

    @Query("SELECT tt FROM ThanhToan tt JOIN tt.lopHoc lop ON tt.lopHoc.idLopHoc = lop.idLopHoc WHERE tt.lopHoc.idLopHoc =:id")
    List<ThanhToan> findByIdLop(@Param("id") Long idLop);

    @Query("SELECT tt FROM ThanhToan tt JOIN tt.hoaDon hd ON tt.hoaDon.idHoaDon = hd.idHoaDon WHERE tt.hoaDon.idHoaDon =:id")
    List<ThanhToan> findByIdHD(@Param("id") Long idHD);
    @Query("SELECT tt FROM ThanhToan tt JOIN tt.nguoiThanhToan hv ON tt.nguoiThanhToan.idUser = hv.idUser WHERE tt.nguoiThanhToan.idUser =:id AND tt.trangThai = :trangThai")
    List<ThanhToan> findByIdHVAndTrangThai(@Param("id") Long idHV, @Param("trangThai") TrangThaiThanhToan trangThai);

    @Query("SELECT tt FROM ThanhToan tt JOIN tt.lopHoc lop ON tt.lopHoc.idLopHoc = lop.idLopHoc WHERE tt.lopHoc.idLopHoc =:id AND tt.trangThai =:trangThai")
    List<ThanhToan> findByIdLopAndTrangThai(@Param("id") Long idLop, @Param("trangThai") TrangThaiThanhToan trangThai);

    @Query("SELECT tt FROM ThanhToan tt JOIN tt.nguoiThanhToan hv ON tt.nguoiThanhToan.idUser = hv.idUser WHERE tt.nguoiThanhToan.idUser =:id  AND tt.hoaDon =null")
    List<ThanhToan> findByIdHVAndHoaDonNUll(@Param("id") Long idHV);
}

