package fit.iuh.backend.service;

import fit.iuh.backend.moudel.BaiTest;
import org.springframework.stereotype.Service;

@Service
public interface BaiTestService {
    BaiTest findById(Long id);
}
