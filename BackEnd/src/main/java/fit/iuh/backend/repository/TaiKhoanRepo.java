package fit.iuh.backend.repository;


import fit.iuh.backend.enumclass.ChucVu;
import fit.iuh.backend.moudel.TaiKhoanLogin;
import fit.iuh.backend.moudel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaiKhoanRepo extends JpaRepository <TaiKhoanLogin,Long>{
       Optional<TaiKhoanLogin> findByTenDangNhap(String tenDangNhap) ;

       @Query("SELECT tk FROM TaiKhoanLogin tk WHERE tk.user.sdt =:sdt")

       Optional<TaiKhoanLogin> findSDT(@Param("sdt") String sdt) ;
       @Query("SELECT tk.user FROM TaiKhoanLogin tk WHERE tk.enable = true AND tk.role=:TrangThai ")
       List<User> findUserByTaiKhoanEnableTrue(@Param("TrangThai") ChucVu chucVu);

//       @Query("SELECT tk FROM TaiKhoanLogin tk WHERE tk.role like %:TrangThai% ")
//       List<TaiKhoanLogin> findTKhoanByTaiKhoanEnable(@Param("TrangThai") String chucVu);
       @Query("SELECT tk FROM TaiKhoanLogin tk WHERE tk.tenDangNhap LIKE %:name% ")
       List<TaiKhoanLogin> findTKhoanLikeName(@Param("name") String name);
       @Query("SELECT tk FROM TaiKhoanLogin tk WHERE tk.enable= true ")
       List<TaiKhoanLogin> findTKhoanTrue();

}
