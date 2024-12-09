package fit.iuh.backend.implement;

import fit.iuh.backend.moudel.KetQuaTest;
import fit.iuh.backend.repository.KetQuaTestRepo;
import fit.iuh.backend.service.KetQuaTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KetQuaTestImplement implements KetQuaTestService {
    @Autowired
    private KetQuaTestRepo KQTRepo;

    @Override
    public KetQuaTest crateKQT(KetQuaTest kq) {
        return KQTRepo.save(kq);
    }

    @Override
    public KetQuaTest findbyId(Long id) {
        return KQTRepo.findById(id).get();
    }

    @Override
    public KetQuaTest findByBTandHV(Long idHocvien, Long idBaiTest) {
        return KQTRepo.findByHocVienAndBaiTest(idBaiTest,idHocvien);
    }

    @Override
    public List<KetQuaTest> findKetQuaTestByHV(Long idHocVien) {
        return KQTRepo.findByHocVien(idHocVien);
    }

    @Override
    public List<KetQuaTest> findKetQuaTestByBT(Long idBaiTest) {
        return KQTRepo.findByBaiTest(idBaiTest);
    }

    @Override
    public List<KetQuaTest> findKetQuaTestByLop(Long idLop) {
        return KQTRepo.findByLop(idLop);
    }


}
