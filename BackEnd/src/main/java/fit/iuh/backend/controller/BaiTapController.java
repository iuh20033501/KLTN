package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.*;
import fit.iuh.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    @Qualifier("buoiHocImplement")
    @Autowired
    private BuoiHocService buoiHoccService;

    @Operation(
            summary = "thêm  bài tap",
            description = """ 
           truyền tên bài tập. ngày BD,ngay KT
            
    """
    )
    @PostMapping("/createBaiTap/{idBuoi}")
    public BaiTap createBaiTap (@PathVariable Long idBuoi,@RequestBody BaiTap baiTap){
        BuoiHoc bh = buoiHoccService.findById(idBuoi);
        baiTap.setBuoiHoc(bh);
        baiTap.setTrangThai(true);
        return  baiTapService.CreateBT(baiTap);
    }
    @GetMapping("/getAll")
    public List<BaiTap> findAllBt(){
        return baiTapService.findAll();
    }
    @GetMapping("/getBaiTapofBuoi/{idBuoi}")
    public List<BaiTap> findBTByBuoi(@PathVariable Long idBuoi){
        return baiTapService.findByIdBuoi(idBuoi);
    }
    @GetMapping("/getBaiTapById/{id}")
    public BaiTap findBTByID(@PathVariable Long id){
        return baiTapService.findById(id);
    }
    @GetMapping("/deleteBtap/{id}")
    public BaiTap deleteBTByID(@PathVariable Long id){
        BaiTap bt = baiTapService.findById(id);
        bt.setTrangThai(false);
        return baiTapService.CreateBT(bt);
    }
    @Operation(
            summary = "kiem tra date tap",
            description = """ 
           
            truyen id Bai Tap  nếu quá ngay hay chưa tới sẽ ko hiện 
    """
    )
    @GetMapping("/kiemtraDate/{id}")
    public BaiTap kiemTra (@PathVariable Long id ){
        BaiTap bt = baiTapService.findById(id);
        Date ngay =new Date();
        if(bt.getNgayBD().after(ngay) && bt.getNgayKT().before(ngay) ) {
            bt.setTrangThai(false);
        }
        return baiTapService.CreateBT(bt);
    }
    @Operation(
            summary = "thêm câu hỏi cho bài tap",
            description = """ 
            truyền tên noi dung, link am thanh, link anh , loi giai
            
    """
    )
    @PostMapping("/createCauHoi/{idBTap}")
    public CauHoi createCauHoi (@PathVariable Long idBTap,@RequestBody CauHoi cauHoi){
        BaiTap bt = baiTapService.findById(idBTap);
        cauHoi.setBaiTest(null);
        cauHoi.setBaiTap(bt);
        CauHoi ch= cauHoiService.createCauHoi(cauHoi);
        return  ch;
    }
    @Operation(
            summary = "tim cau hoi theo id Bai tap",
            description = """ 
            truyen id Bai tap     
    """
    )
    @GetMapping("/getCauHoi/{idTap}")
    public List<CauHoi> getListCauHoi (@PathVariable Long idTap){
        List<CauHoi> list = cauHoiService.findByIdBaiTap(idTap);
        return list;
    }
    @GetMapping("/getAllCauHoi")
    public List<CauHoi> getListAll (){
        List<CauHoi> list = cauHoiService.findAll();
        return list;
    }

    @Operation(
            summary = "get câu tra lời",
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
            summary = "get tiến trình của học viên trong bài tập",
            description = """ 
            truyền id hoc vien và id bài tập ra danh sách câu trả lời
            
    """
    )
    @GetMapping("/getTienTrinh/{idHocVien}/{idBaiTap}")
    public TienTrinh getListTienTrih(@PathVariable Long idHocVien,@PathVariable Long idBaiTap){
        TienTrinh tt = tienTrinhService.findByIdHvIdBTap(idHocVien, idBaiTap);
        return  tt;
    }
    @Operation(
            summary = "get tiến trình của học viên ",
            description = """ 
            truyền id hoc vien  ra danh sách câu trả lời
            
    """
    )
    @GetMapping("/getTienTrinhofHV/{idHocVien}")
    public List<TienTrinh> getListTienTrihofHV(@PathVariable Long idHocVien){
        List<TienTrinh> tt = tienTrinhService.findByIdHv(idHocVien);
        return  tt;
    }
    @Operation(
            summary = "get tiến trình  trong bài tập",
            description = """ 
           n và id bài tập ra danh sách câu trả lời
            
    """
    )
    @GetMapping("/getTienTrinhofBT/{idBaiTap}")
    public List<TienTrinh> getListTienTrihofBaiTap(@PathVariable Long idBaiTap){
        List<TienTrinh> tt = tienTrinhService.findByIdBatTap( idBaiTap);
        return  tt;
    }
    //
    @Operation(
            summary = "tạo và update tiên trình khi làm  bài tập",
            description = """
            load danh sách câu hỏi trước câu trả lời đúng cũng là só thứ tự câu hỏi cần load
            khi chưa làm thì khi nhấn vào sẽ tạo ra 
            chạy khi câu tra lời đúng thì chạy hàm vào update tiến trình lên số câu đúng vưa tính điểm 
            vừa là số câu hỏi trông list câu hỏi của bài tâp . 
            
            truyền id bai test, học viên.
            
    """
    )
    @GetMapping("/updateTienTrinh/{idHocvien}/{idBaiTap}")
    public TienTrinh lamBaiTest(@PathVariable Long idHocVien,@PathVariable Long idBaiTap){

        TienTrinh tienTrinh = tienTrinhService.findByIdHvIdBTap(idHocVien, idBaiTap);
        if(tienTrinh==null) {
            BaiTap baiTap = baiTapService.findById(idBaiTap);
            HocVien hocVien = hocVienService.findByIdHocVien(idHocVien).orElseThrow(() -> new RuntimeException("Hoc Vien not found"));
            return tienTrinhService.createTT(new TienTrinh(0l, hocVien, baiTap));
        }else{
            Long soCau = tienTrinh.getCauDung();
            tienTrinh.setCauDung(soCau++);
            return tienTrinhService.createTT(tienTrinh);
        }
    }


}
