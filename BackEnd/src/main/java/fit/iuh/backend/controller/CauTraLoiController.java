package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.CauHoi;
import fit.iuh.backend.moudel.CauTraLoi;
import fit.iuh.backend.service.CauHoiService;
import fit.iuh.backend.service.CauTraLoiService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cauTraLoi")
public class CauTraLoiController {
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
    @GetMapping("/getAll")
    public List<CauTraLoi> findAll(){
        return service.findAll();
    }
    @GetMapping("/findById/{idCauTraLoi}")
    public CauTraLoi createAwser( @PathVariable Long idCauTraLoi){
        return service.findById(idCauTraLoi);
    }
    @Operation(
            summary = "them câu trả lời cho câu hỏi",
            description = """ 
          idcauhoi và idcautraloi muon update.
          tuyèn nội dung, ket quả vào câu trả lời.
    """
    )
    @PutMapping("/create/{idCauHoi}/{idCauHoiTraLoi}")
    public CauTraLoi createAwser(@RequestBody CauTraLoi c, @PathVariable Long idCauHoi,@PathVariable Long idCauHoiTraLoi){
        CauHoi cauHoi = cauHoiService.findById(idCauHoi);
        c.setCauHoi(cauHoi);
        c.setIdCauTraLoi(idCauHoiTraLoi);
        return service.createCauTraLoi(c);
    }
}
