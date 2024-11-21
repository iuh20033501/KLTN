package fit.iuh.backend.repository;


import fit.iuh.backend.moudel.TaiKhoanLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TaiKhoanRepo extends JpaRepository <TaiKhoanLogin,Long>{
       Optional<TaiKhoanLogin> findByTenDangNhap(String tenDangNhap) ;

       @Query("SELECT tk FROM TaiKhoanLogin tk WHERE tk.user.sdt =:sdt")

       Optional<TaiKhoanLogin> findSDT(@Param("sdt") String sdt) ;
}
