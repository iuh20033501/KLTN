package fit.iuh.backend.controller;

import fit.iuh.backend.dto.TinhDiemTestDTO;
import fit.iuh.backend.moudel.*;
import fit.iuh.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/baitest")
public class BaiTestController {
    @Autowired
    private CauHoiService cauHoiService;
    @Autowired
    private HocVienService hocVienService;
    @Autowired
    private BaiTestService baiTestService;
    @Autowired
    private CauTraLoiService cauTraLoiService;
    @Autowired
    private KetQuaTestService ketQuaTestService;
    Long soLuongCauHoi ;
    @GetMapping("/getCauHoi/{id}")
    public List<CauHoi> getListCauHoi (@PathVariable Long id){
        List<CauHoi> list = cauHoiService.findByIdBaiTest(id);
        soLuongCauHoi = (long) list.size();

        return list;
    }
    @PostMapping("/create")
    public BaiTest createBaiTest(@RequestBody BaiTest baiTest){
        return baiTestService.createBaiTest(baiTest);
    }
    @GetMapping("/getBaiTest/{id}")
    public BaiTest findById (@PathVariable Long id){
        return baiTestService.findById(id);
    }
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
    //
    @PostMapping("/lamBai")
    public KetQuaTest lamBaiTest(@RequestBody TinhDiemTestDTO tinhDiemDTO){
        long soCauDung = 0L;

        long diemSo;
        List<CauTraLoi> list = tinhDiemDTO.getListCauTraLoi();
        for(CauTraLoi l:list){
            if(l.getKetQua().equals(true)) soCauDung++;
        }
        BaiTest baiTest = baiTestService.findById(tinhDiemDTO.getIdBaiTest());
        HocVien hocVien = hocVienService.findByIdHocVien(tinhDiemDTO.getIdHocVien()).orElseThrow(()->new RuntimeException("HocVien not found"));
        diemSo =soCauDung/soLuongCauHoi;
        return ketQuaTestService.crateKQT(new KetQuaTest(diemSo,baiTest,hocVien));
    }



}
