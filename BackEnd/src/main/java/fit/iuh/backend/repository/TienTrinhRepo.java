package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.TienTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TienTrinhRepo extends JpaRepository<TienTrinh,Long> {
    @Query("select tt from TienTrinh  tt join hocvien hv on tt.hocVien.idUser= hv.idUser where tt.hocVien.idUser= :id")
    List<TienTrinh> getByIdHV (Long idHV);
    @Query("select tt from TienTrinh  tt join BaiTap bt on tt.baiTap.idBaiTap= bt.idBaiTap where tt.baiTap.idBaiTap= :id")
    List<TienTrinh> getByIdBT (Long idBT);
}
