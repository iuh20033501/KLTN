package fit.iuh.backend.implement;

import fit.iuh.backend.moudel.BaiTest;
import fit.iuh.backend.repository.BaiTestRepo;
import fit.iuh.backend.service.BaiTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaiTestImplement implements BaiTestService {
    @Autowired
    private BaiTestRepo baiTestRepo;
    @Override
    public BaiTest findById(Long id) {
        return baiTestRepo.findById(id).get();
    }
}
