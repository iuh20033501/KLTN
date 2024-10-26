package fit.iuh.backend.implement;

import fit.iuh.backend.moudel.CauTraLoi;
import fit.iuh.backend.repository.CauTraLoiRepo;
import fit.iuh.backend.service.CauTraLoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CauTraLoiImplement implements CauTraLoiService {
    @Autowired
    private CauTraLoiRepo cauTraLoirepo;
    @Override
    public List<CauTraLoi> findByIdCauHoi(Long id) {
        return cauTraLoirepo.findByIdCauHoi(id);
    }

    @Override
    public CauTraLoi findById(Long id) {
        return cauTraLoirepo.findById(id).get();
    }

    @Override
    public CauTraLoi createCauTraLoi(CauTraLoi ctl) {
        return cauTraLoirepo.save(ctl);
    }

    @Override
    public List<CauTraLoi> findAll() {
        return cauTraLoirepo.findAll();
    }
}
