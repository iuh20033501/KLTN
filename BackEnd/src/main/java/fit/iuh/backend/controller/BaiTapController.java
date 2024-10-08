package fit.iuh.backend.controller;

import fit.iuh.backend.dto.TinhDiemBaiTapDTO;
import fit.iuh.backend.dto.TinhDiemTestDTO;
import fit.iuh.backend.moudel.*;
import fit.iuh.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/baitap")
public class BaiTapController {
    @Autowired
    private CauHoiService cauHoiService;
    @Autowired
    private CauTraLoiService cauTraLoiService;
    @Autowired
    private BaiTapService baiTapService;
    @Autowired
    private TienTrinhService tienTrinhService;
    @Autowired
    private HocVienService hocVienService;


    @PostMapping("/createCauHoi")
    public CauHoi createCauHoi (@RequestBody CauHoi cauHoi){
        CauHoi ch= cauHoiService.createCauHoi(cauHoi);
        return  ch;
    }
    @GetMapping("/getCauTraLoi/{idCauHoi}")
    public List<CauTraLoi> getListCauTraLloi(@PathVariable Long idCauHoi){
        List<CauTraLoi> list = cauTraLoiService.findByIdCauHoi(idCauHoi);
        return  list;
    }

    @PostMapping("/lamBai")
    public TienTrinh lamBaiTest(@RequestBody TinhDiemBaiTapDTO tinhDiemDTO){
        Long soCauDung = 0l;

        Long diemSo;
        List<CauTraLoi> list = tinhDiemDTO.getListCauTraLoi();
        for(CauTraLoi l:list){
            if(l.getKetQua().equals(true)) soCauDung++;
        }
        BaiTap baiTap = baiTapService.findById(tinhDiemDTO.getIdBaiTap());
        HocVien hocVien = hocVienService.findByIdHocVien(tinhDiemDTO.getIdHocVien());
        diemSo =soCauDung/10;
        return tienTrinhService.createTT(new TienTrinh(diemSo,hocVien,baiTap));
    }
}
