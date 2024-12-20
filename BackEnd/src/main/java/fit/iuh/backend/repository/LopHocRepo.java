package fit.iuh.backend.repository;

import fit.iuh.backend.enumclass.TrangThaiLop;
import fit.iuh.backend.moudel.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LopHocRepo extends JpaRepository<LopHoc, Long> {
    @Query("SELECT l FROM LopHoc l JOIN l.giangVien gv WHERE gv.idUser = :id")
    List<LopHoc> getListLopByGiaoVien(@Param("id") Long idGv);

    @Query("SELECT l FROM LopHoc l JOIN l.khoaHoc k WHERE k.idKhoaHoc = :id")
    List<LopHoc> getListLopByKhoaHoc(@Param("id") Long idKhoa);

    @Query("SELECT l FROM LopHoc l WHERE l.idLopHoc = :id")
    List<LopHoc> getListLikeId(@Param("id") Long idLopHoc);

    @Query("SELECT l FROM LopHoc l WHERE l.tenLopHoc LIKE %:name%")
    List<LopHoc> getListLikeTen(@Param("name") String name);

    @Query("SELECT l FROM LopHoc l JOIN l.khoaHoc k WHERE k.tenKhoaHoc LIKE %:name%")
    List<LopHoc> getListLopLikenameKhoaHoc(@Param("name") String name);

    @Query("SELECT l FROM LopHoc l JOIN l.giangVien gv WHERE gv.hoTen LIKE %:name%")
    List<LopHoc> getListLopLikenameGiaoVien(@Param("name") String name);
    @Query("SELECT l FROM LopHoc l WHERE l.trangThai=:trangThai")
    List<LopHoc> getListByTrangThai(@Param("trangThai")  TrangThaiLop trangThai);
    @Query("SELECT l FROM LopHoc l WHERE l.trangThai IN :trangThais")
    List<LopHoc> getListByTrangThaiInList(@Param("trangThais") List<TrangThaiLop> trangThais);

}
