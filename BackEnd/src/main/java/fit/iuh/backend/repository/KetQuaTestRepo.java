package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.KetQuaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KetQuaTestRepo extends JpaRepository<KetQuaTest,Long> {
    @Query("select kq from KetQuaTest  kq join BaiTest b on kq.baiTest.idTest= b.idTest where kq.baiTest.idTest=:id")
    KetQuaTest findByBaiTest(@Param("id") Long id );
}
