package fit.iuh.backend.service;

import fit.iuh.backend.moudel.CauHoi;
import fit.iuh.backend.moudel.CauTraLoi;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CauTraLoiService {
    List<CauTraLoi> findByIdCauHoi (Long id);
    CauTraLoi findById (Long id);
    CauTraLoi createCauTraLoi(CauTraLoi ctl);
}
