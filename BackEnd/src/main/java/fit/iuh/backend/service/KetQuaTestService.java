package fit.iuh.backend.service;

import fit.iuh.backend.moudel.KetQuaTest;
import org.springframework.stereotype.Service;

@Service
public interface KetQuaTestService {
    KetQuaTest crateKQT(KetQuaTest kq);
    KetQuaTest findbyId (Long id);
}
