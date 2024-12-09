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
        baiTest.setTrangThai(true);
        baiTest.setXetDuyet(false);
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
    @GetMapping("/getBaiTestofXetDuetFalse/{idLop}")
    public List<BaiTest> findBTByXetDuyetFalse(@PathVariable Long idLop){
        return baiTestService.finByIdLopTrue(idLop);
    }
//    @PostMapping("/setListBaiTestofXetDuetFalse/{accept}")
//    public List<BaiTest> findBTByXetDuyetFalse(@PathVariable Boolean accept, @RequestBody List<Long> list){
//        if(accept = true){
//            for(list: Long id ){
//
//            }
//
//        }
//        return baiTestService.finByIdLopTrue(idLop);
//    }
//
    @Operation(
            summary = "get bai test theo idLop ",
            description = """     
    """
    )
    @GetMapping("/getBaiTestofLop/{idLop}")
    public List<BaiTest> findBTByLop(@PathVariable Long idLop){
        return baiTestService.finByIdLop(idLop);
    }

    @GetMapping("/getBaiTestofLopXetFalse/{idLop}")
    public List<BaiTest> findBTByLopXetFalse(@PathVariable Long idLop){
        return baiTestService.finByIdLopTrueXetDUyetFalse(idLop);
    }

    @PostMapping("/AceptBaiTestofLopXetFalse")
    public Boolean aceptBTByLopXetFalse(@RequestBody List<Long> listLong) {
        try {
            for (Long id : listLong) {
                BaiTest baiTest = baiTestService.findById(id);
                if (baiTest != null) { // Kiểm tra nếu BaiTest tồn tại
                    baiTest.setXetDuyet(true);
                    baiTestService.createBaiTest(baiTest);
                }
            }
            return true; // Thành công
        } catch (Exception ex) {
            ex.printStackTrace();
            return false; // Gặp lỗi
        }
    }
    @PostMapping("/CancelBaiTestofLopXetFalse")
    public Boolean cancelBTByLopXetFalse(@RequestBody List<Long> listLong) {
        try {
            for (Long id : listLong) {
                BaiTest baiTest = baiTestService.findById(id);
                if (baiTest != null) { // Kiểm tra nếu BaiTest tồn tại
                    baiTest.setTrangThai(false);
                    baiTestService.createBaiTest(baiTest);
                }
            }
            return true; // Thành công
        } catch (Exception ex) {
            ex.printStackTrace();
            return false; // Gặp lỗi
        }
    }
    @GetMapping("/deleteBaiTest/{id}")
    public BaiTest deleteBT(@PathVariable Long id){
        BaiTest bt = baiTestService.findById(id);
        bt.setTrangThai(false);
       BaiTest btestFilnal = baiTestService.createBaiTest(bt);
        return  btestFilnal;
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
    tạo ra kết quả 
    nếu có kêta qua học viên ở bài test đó sẽ trả vè null
    """
    )
    @PostMapping("/lamBai/createKetQua/{idBaiTest}/{idHocVien}")
    public KetQuaTest lamBaiTest(@RequestBody KetQuaTest ketQua, @PathVariable Long idBaiTest, @PathVariable Long idHocVien) {
        BaiTest baiTest = baiTestService.findById(idBaiTest);
        HocVien hocVien = hocVienService.findByIdHocVien(idHocVien).orElseThrow(()->new RuntimeException("hoc vien not found "));

        if (baiTest != null && hocVien != null) {
            // Tìm kiếm kết quả hiện tại dựa trên học viên và bài test
            KetQuaTest existingKetQua = ketQuaTestService.findByBTandHV(idHocVien, idBaiTest);

            if (existingKetQua == null) {
                // Nếu chưa tồn tại, tạo mới kết quả
                ketQua.setHocVien(hocVien);
                ketQua.setBaiTest(baiTest);
                return ketQuaTestService.crateKQT(ketQua);
            }
        }
        return null;
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
    @GetMapping("/getKetQuaByLop/{idLop}")
    public List<KetQuaTest> getKetQuaByLop (@PathVariable Long idLop){
        List<KetQuaTest> list = ketQuaTestService.findKetQuaTestByLop(idLop);
        return list;
    }
}
