package fit.iuh.backend.service;

import fit.iuh.backend.moudel.KetQuaTest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KetQuaTestService {
    KetQuaTest crateKQT(KetQuaTest kq);
    KetQuaTest findbyId (Long id);
    KetQuaTest findByBTandHV(Long idHocvien,Long idBaiTest);
    List<KetQuaTest> findKetQuaTestByHV (Long idHocVien);
    List<KetQuaTest> findKetQuaTestByBT (Long idBaiTest);
    List<KetQuaTest> findKetQuaTestByLop (Long idLop);
}
