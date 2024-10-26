package fit.iuh.backend.implement;

import fit.iuh.backend.moudel.BaiTap;
import fit.iuh.backend.repository.BaiTapRepo;
import fit.iuh.backend.service.BaiTapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaiTapImplement implements BaiTapService {
    @Autowired
    private BaiTapRepo baiTapRepo;

    @Override
    public BaiTap CreateBT(BaiTap baiTap) {
        return baiTapRepo.save(baiTap);
    }

    @Override
    public BaiTap findById(Long id) {
        return baiTapRepo.findById(id).get();
    }

    @Override
    public List<BaiTap> findAll() {
        return baiTapRepo.findAll();
    }

    @Override
    public List<BaiTap> findByIdBuoi(Long idBuoi) {
        return baiTapRepo.findByIdBuoiHoc(idBuoi);
    }

//    @Override
//    public List<BaiTap> findByIdHV(Long idHV) {
//        return baiTapRepo.FindByIdHV(idHV);
//    }
}
