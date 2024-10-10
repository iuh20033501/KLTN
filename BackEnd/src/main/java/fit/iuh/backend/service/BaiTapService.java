package fit.iuh.backend.service;

import fit.iuh.backend.moudel.BaiTap;
import fit.iuh.backend.moudel.BaiTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaiTapService {
  BaiTap CreateBT (BaiTap baiTap);
  BaiTap findById(Long id);
  List<BaiTap> findAll();
  List<BaiTap> findByIdBuoi(Long idBuoi);
//  List<BaiTap> findByIdHV(Long idHV);
}
