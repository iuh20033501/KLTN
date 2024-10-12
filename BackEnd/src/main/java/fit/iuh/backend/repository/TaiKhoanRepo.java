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

       @Query("select tk from TaiKhoanLogin tk join User u on tk.user.idUser =u.idUser where tk.user.sdt= :id")
       Optional<TaiKhoanLogin> findSDT(@Param("id") String id) ;
}
