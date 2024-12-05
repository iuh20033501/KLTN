package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.HoaDon;
import fit.iuh.backend.moudel.KhoaHoc;
import fit.iuh.backend.moudel.LopHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhoaHocRepo extends JpaRepository<KhoaHoc,Long> {
    @Query("select khoa from KhoaHoc khoa where substring(khoa.thoiGianDienRa, 1, 4) = :nam")
    List<KhoaHoc> findKhoaHocByYear(@Param("nam") String nam);
    @Query("SELECT khoa FROM KhoaHoc khoa WHERE khoa.tenKhoaHoc LIKE %:name%")
    List<KhoaHoc> getListLikeTen(@Param("name") String name);
    @Query("SELECT khoa FROM KhoaHoc khoa WHERE khoa.trangThai= true and khoa.tenKhoaHoc LIKE %:name%")
    List<KhoaHoc> getListActiveTrue(String name);

}
