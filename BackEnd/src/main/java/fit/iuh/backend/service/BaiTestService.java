package fit.iuh.backend.service;

import fit.iuh.backend.moudel.BaiTap;
import fit.iuh.backend.moudel.BaiTest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaiTestService {
    BaiTest findById(Long id);
    List<BaiTest> finByIdLop(Long idLop);

    BaiTest createBaiTest (BaiTest bt);
    List<BaiTest> findAll(BaiTest bt) ;

}
