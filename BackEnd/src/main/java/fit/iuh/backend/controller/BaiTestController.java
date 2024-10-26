package fit.iuh.backend.controller;

import fit.iuh.backend.dto.TinhDiemTestDTO;
import fit.iuh.backend.moudel.*;
import fit.iuh.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    @Autowired
    private LopHocService lopHocService;
    Long soLuongCauHoi ;
    @Operation(
            summary = "tim cau hoi theo id Bai test",
            description = """ 
            truyen id Bai Test     
    """
    )
    @GetMapping("/getCauHoi/{idTest}")
    public List<CauHoi> getListCauHoi (@PathVariable Long idTest){
        List<CauHoi> list = cauHoiService.findByIdBaiTest(idTest);
        soLuongCauHoi = (long) list.size();

        return list;
    }
    @Operation(
            summary = "thêm bài test",
            description = """ 
            đối tuộng có loại test(enum), thoi gian lam bai, thoi gian ket thuc
            truyen id Bai Test     
    """
    )
    @PostMapping("/create/{idLop}")
    public BaiTest createBaiTest(@PathVariable Long idLop,@RequestBody BaiTest baiTest){
        LopHoc lop = lopHocService.findById(idLop).get();
        baiTest.setLopHoc(lop);
        baiTest.setTrangThai(true);
        return baiTestService.createBaiTest(baiTest);
    }
    @GetMapping("/deleteBaiTest/{id}")
    public BaiTest deleteBT(@PathVariable Long id){
        BaiTest bt = baiTestService.findById(id);
        bt.setTrangThai(false);
        return bt;
    }
    @Operation(
            summary = "kiem tra date test",
            description = """ 
           
            truyen id Bai Test  nếu quá nagyf hay chưa tới sẽ ko hiện 
    """
    )
    @GetMapping("/kiemtraDate/{id}")
    public BaiTest kiemTra (@PathVariable Long id ){
        BaiTest bt = baiTestService.findById(id);
        Date ngay =new Date();
        if(bt.getNgayBD().after(ngay) && bt.getNgayKT().before(ngay) ) {
            bt.setTrangThai(false);
        }
        return baiTestService.createBaiTest(bt);
    }
    @GetMapping("/getBaiTest/{idbaiTest}")
    public BaiTest findById (@PathVariable Long idbaiTest){
        return baiTestService.findById(idbaiTest);
    }
    @Operation(
            summary = "thêm câu hỏi cho bài test",
            description = """ 
            truyền tên noi dung, link am thanh, link anh , loi giai
            
    """
    )
    @PostMapping("/createCauHoi/{idTest}")
    public CauHoi createCauHoi (@PathVariable Long idTest,@RequestBody CauHoi cauHoi){
        BaiTest bt = baiTestService.findById(idTest);
        cauHoi.setBaiTest(bt);
        cauHoi.setBaiTap(null);
        CauHoi ch= cauHoiService.createCauHoi(cauHoi);
        return  ch;
    }
    @Operation(
            summary = "get cau tra lời",
            description = """ 
            truyền id cau hỏi ra danh sách câu trả lời
            
    """
    )
    @GetMapping("/getCauTraLoi/{idCauHoi}")
    public List<CauTraLoi> getListCauTraLloi(@PathVariable Long idCauHoi){
        List<CauTraLoi> list = cauTraLoiService.findByIdCauHoi(idCauHoi);
        return  list;
    }
    @Operation(
            summary = "làm  bài test",
            description = """ 
            **lưu ý cần chạy getList cau hỏi đẻ biết có bao nhiêu câu hỏi trong bài dẻ tính điểm
            truyền id bai test, học viên,tgian lam bai và danh sách cau trả lời học viên làm bài dã chọn  ra danh sách câu trả lời
            
    """
    )
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
        return ketQuaTestService.crateKQT(new KetQuaTest(diemSo,tinhDiemDTO.getThoigianLamBai(),baiTest,hocVien));
    }

}
