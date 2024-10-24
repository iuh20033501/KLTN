package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.CauHoi;
import fit.iuh.backend.moudel.CauTraLoi;
import fit.iuh.backend.service.CauHoiService;
import fit.iuh.backend.service.CauTraLoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cauhoi")
public class CauHoiController {
    @Qualifier("cauTraLoiImplement")
    @Autowired
    private CauTraLoiService service;
    @Autowired
    private CauHoiService cauHoiService;
    @PostMapping("/create/{idCauHoi}")
    public CauTraLoi createAwser(@RequestBody CauTraLoi c, @PathVariable Long idCauHoi){
        CauHoi cauHoi = cauHoiService.findById(idCauHoi);
        c.setCauHoi(cauHoi);
        return service.createCauTraLoi(c);
    }
}
