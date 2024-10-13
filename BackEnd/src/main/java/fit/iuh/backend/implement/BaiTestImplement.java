package fit.iuh.backend.implement;

import fit.iuh.backend.moudel.BaiTest;
import fit.iuh.backend.repository.BaiTestRepo;
import fit.iuh.backend.service.BaiTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaiTestImplement implements BaiTestService {
    @Autowired
    private BaiTestRepo baiTestRepo;
    @Override
    public BaiTest findById(Long id) {
        return baiTestRepo.findById(id).get();
    }

    @Override
    public List<BaiTest> finByIdLop(Long idLop) {
        return baiTestRepo.getListTestByIdLop(idLop);
    }


    @Override
    public BaiTest createBaiTest(BaiTest bt) {
        return baiTestRepo.save(bt);
    }

    @Override
    public List<BaiTest> findAll(BaiTest bt) {
        return baiTestRepo.findAll();
    }
}
