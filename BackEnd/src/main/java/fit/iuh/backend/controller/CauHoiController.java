package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.CauTraLoi;
import fit.iuh.backend.service.CauTraLoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cauhoi")
public class CauHoiController {
    @Qualifier("cauTraLoiImplement")
    @Autowired
    private CauTraLoiService service;
    @PostMapping("/create")
    public CauTraLoi createAwser(@RequestBody CauTraLoi c){
        return service.createCauTraLoi(c);
    }
}
