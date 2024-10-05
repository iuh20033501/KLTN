package fit.iuh.backend.implement;

import fit.iuh.backend.moudel.KetQuaTest;
import fit.iuh.backend.repository.KetQuaTestRepo;
import fit.iuh.backend.service.KetQuaTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
