package fit.iuh.backend.controller;

import fit.iuh.backend.dto.TinhDiemTestDTO;
import fit.iuh.backend.moudel.*;
import fit.iuh.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
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
//        soLuongCauHoi = (long) list.size();
        return list;
    }
    @Operation(
            summary = "tim cau hoi theo id Bai test và có trạng thái True",
            description = """ 
            truyen id Bai Test     
    """
    )
    @GetMapping("/getCauHoiTrue/{idTest}")
    public List<CauHoi> getListCauHoiandTrangThaiTrue (@PathVariable Long idTest){
        List<CauHoi> list = cauHoiService.findByIdBaiTestandTrangThaiTrue(idTest);
        soLuongCauHoi = (long) list.size();
        return list;
    }
    @Operation(
            summary = "thêm bài test",
            description = """ 
            đối tuộng có loại test(enum), thoi gian lam bai(dạng Time), thoi gian kết thuc
            truyen id Bai Lop     
    """
    )
    @PostMapping("/create/{idLop}")
    public BaiTest createBaiTest(@PathVariable Long idLop,@RequestBody BaiTest baiTest){
        LopHoc lop = lopHocService.findById(idLop).get();
        baiTest.setLopHoc(lop);
        baiTest.setTrangThai(false);
        return baiTestService.createBaiTest(baiTest);
    }
    @Operation(
            summary = "get bai test theo idLop có trang thái true",
            description = """     
    """
    )
    @GetMapping("/getBaiTestofLopTrue/{idLop}")
    public List<BaiTest> findBTByLopTrue(@PathVariable Long idLop){
        return baiTestService.finByIdLopTrue(idLop);
    }
    @Operation(
            summary = "get bai test theo idLop ",
            description = """     
    """
    )
    @GetMapping("/getBaiTestofLop/{idLop}")
    public List<BaiTest> findBTByLop(@PathVariable Long idLop){
        return baiTestService.finByIdLop(idLop);
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
           
            truyen id Bai Test  nếu quá ngay hay chưa tới sẽ ko hiện 
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
            summary = "delete hoi theo idCauHoi",
            description = """ 
            truyen idCauHoi 
    """
    )
    @GetMapping("/deleteCauHoi/{idCauHoi}")
    public CauHoi deleteCauHoi (@PathVariable Long idCauHoi){
        CauHoi cauHoi = cauHoiService.findById(idCauHoi);
        cauHoi.setTrangThai(false);
        return cauHoiService.createCauHoi(cauHoi);
    }
    @Operation(
            summary = "update cau tra lời",
            description = """ 
            truyền id cau hỏi, idBaiTest vao param
            nhap cauhoi dầy đủ thông tin dẻ cap nhật trừ id.
            
    """
    )
    @PutMapping("/updateCauHoi/{idCauHoi}/{idBaiTest}")
    public CauHoi updateCauHoi(@PathVariable Long idCauHoi, @PathVariable Long idBaiTest, @RequestBody CauHoi cauHoi) {
        BaiTest bt = baiTestService.findById(idBaiTest);
        CauHoi ch = cauHoiService.findById(idCauHoi);

        if (ch != null && bt != null) {
            cauHoi.setIdCauHoi(idCauHoi);
            cauHoi.setBaiTap(null);
            cauHoi.setBaiTest(bt);
            return cauHoiService.createCauHoi(cauHoi); // Gọi phương thức update thay vì create
        }

        // Trả về null hoặc có thể ném một ngoại lệ nếu không tìm thấy ch hoặc bt
        throw new ConfigDataResourceNotFoundException(null);
    }
    @Operation(
            summary = "Làm bài test",
            description = """ 
    **Lưu ý:**
    1. Cần chạy `getListTrue` theo ID bài test để biết có bao nhiêu câu hỏi trong bài, từ đó tính điểm.
    2. Truyền vào các tham số:
        - `idBaiTest`: ID của bài test.
        - `idHocVien`: ID của học viên.
        - `thoigianLamBai` (String): Thời gian hoàn thành bài test.
        - số câu đúng (long): số câu đúng
    3. Nếu bài test chưa được làm, tạo mới và set thời gian `timeReset`.
    4. Nếu thời gian đã qua (so với `timeReset`), không cho phép cập nhật nữa, trả về `null`.Ngược lại sẽ update theo kq có sẵn
    """
    )
    @PostMapping("/lamBai")
    public KetQuaTest lamBaiTest(@RequestBody TinhDiemTestDTO tinhDiemDTO){
        KetQuaTest kq = ketQuaTestService.findByBTandHV(tinhDiemDTO.getIdHocVien(), tinhDiemDTO.getIdBaiTest());
        Date current = new Date();
        Long soCauDung = tinhDiemDTO.getSoCauDung();
        Long diemSo = soCauDung / soLuongCauHoi;

        // Kiểm tra nếu chưa có kết quả thì tạo mới
        if (kq == null){
            BaiTest baiTest = baiTestService.findById(tinhDiemDTO.getIdBaiTest());
            HocVien hocVien = hocVienService.findByIdHocVien(tinhDiemDTO.getIdHocVien())
                    .orElseThrow(() -> new RuntimeException("HocVien not found"));

            // Lấy thời gian làm bài và cộng thêm thời gian để tính reset
            long thoiGianLamBai = baiTest.getThoiGianLamBai().getTime(); // Thời gian làm bài là kiểu Time
            long timeRetest = current.getTime() + thoiGianLamBai; // Thêm thời gian làm bài vào thời gian hiện tại

            return ketQuaTestService.crateKQT(new KetQuaTest(diemSo, tinhDiemDTO.getThoigianLamBai(), new java.sql.Date(timeRetest), baiTest, hocVien));
        }
        // Nếu đã có kết quả, kiểm tra thời gian
        else {
            if (current.before(kq.getTimeRetest())) {
                kq.setDiemTest(diemSo);
                kq.setThoiGianHoanThanh(tinhDiemDTO.getThoigianLamBai());
                return ketQuaTestService.crateKQT(kq);
            } else {
                // Nếu quá thời gian, trả về null
                return null;
            }
        }
    }

    @Operation(
            summary = "get ket quả test theo id",
            description = """ 
            truyen idketquatest 
    """
    )
    @GetMapping("/getKetQua/{idKetQuaTest}")
    public KetQuaTest getKetQua (@PathVariable Long idKetQuaTest){
        KetQuaTest ketQuaTest = ketQuaTestService.findbyId(idKetQuaTest);
        return ketQuaTest;
    }
    @Operation(
            summary = "getList ket quả test theo idTest",
            description = """ 
            truyen idBaiTest 
    """
    )
    @GetMapping("/getKetQuaByTest/{idBaiTest}")
    public List<KetQuaTest> getKetQuaByTest (@PathVariable Long idBaiTest){
        List<KetQuaTest> list = ketQuaTestService.findKetQuaTestByBT(idBaiTest);
        return list;
    }
    @Operation(
            summary = "getList ket quả test theo idHocVien",
            description = """ 
            truyen idHocVien 
    """
    )
    @GetMapping("/getKetQuaByHV/{idHocVien}")
    public List<KetQuaTest> getKetQuaByHV (@PathVariable Long idHocVien){
        List<KetQuaTest> list = ketQuaTestService.findKetQuaTestByHV(idHocVien);
        return list;
    }
    @Operation(
            summary = "get ket quả test theo id",
            description = """ 
            truyen idketquatest 
    """
    )
    @GetMapping("/getKetQua/{idBaiTest}/{idHocVien}")
    public KetQuaTest getKetQuaByHVAndBT (@PathVariable Long idBaiTest,@PathVariable Long idHocVien){
        KetQuaTest ketQuaTest = ketQuaTestService.findByBTandHV(idHocVien,idBaiTest);
        return ketQuaTest;
    }
}
