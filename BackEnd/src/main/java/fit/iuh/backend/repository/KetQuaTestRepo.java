package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.KetQuaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KetQuaTestRepo extends JpaRepository<KetQuaTest,Long> {
    @Query("select kq from KetQuaTest  kq join BaiTest b on kq.baiTest.idTest= b.idTest where kq.baiTest.idTest=:id")
    List<KetQuaTest> findByBaiTest(@Param("id") Long id );
    @Query("select kq from KetQuaTest  kq join hocvien hv on kq.hocVien.idUser= hv.idUser where kq.hocVien.idUser=:id")
    List<KetQuaTest> findByHocVien(@Param("id")Long id);
    @Query("select kq from KetQuaTest kq " +
            "where kq.baiTest.idTest = :idBaiTest " +
            "and kq.hocVien.idUser = :idHocVien")
    KetQuaTest findByHocVienAndBaiTest(@Param("idBaiTest") Long idBaiTest, @Param("idHocVien") Long idHocVien);

}
