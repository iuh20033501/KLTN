package fit.iuh.backend.controller;

import fit.iuh.backend.enumclass.ThanhToanEnum;
import fit.iuh.backend.moudel.*;
import fit.iuh.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/thanhToan")
public class ThanhToanController {
    @Autowired
    private ThanhToanService thanhToanService;
//    @Qualifier("buoiHocImplement")
    @Autowired
    private LopHocService lopHocService;
    @Autowired
    private HocVienService hocVienService;
    @Autowired
    private HoaDonService hoaDonService;
    @Operation(
            summary = "Thêm thanh toan",
            description = """
           
            idLop, idHocVien
            tự động tạo mọi thanh toán ở trạng thái wait
    """
    )
    @PostMapping("/create/{idLop}/{idHocVien}")
    public ThanhToan createTaiLieu (@PathVariable Long idLop,@PathVariable Long idHocVien){
        LopHoc lop =lopHocService.findById(idLop).orElseThrow(() -> new RuntimeException("Lop hoc not found"));
        HocVien hocVien = hocVienService.findByIdHocVien(idHocVien).orElseThrow(() -> new RuntimeException("Hoc vien not found"));
//        HoaDon hoaDon = hoaDonService.findById(idHoaDon).orElseThrow(() -> new RuntimeException("Hoa don not found"));
        ThanhToan thanhToan = new ThanhToan();
        thanhToan.setNguoiThanhToan(hocVien);
        thanhToan.setHoaDon(null);
        thanhToan.setLopHoc(lop);
        thanhToan.setTrangThai(ThanhToanEnum.WAIT);
        return thanhToanService.createThanhToan(thanhToan);
    }
    @Operation(
            summary = "update thanh toan thanh công",
            description = """
           
            idLop, idHocVien. idhoadon, id thanhToaN
            Update ThanHtOAN THÀNH CÔNG
    """
    )
    @PostMapping("/create/{idLop}/{idHocVien}/{idHoaDon}/{idThanhToan}")
    public ThanhToan createTaiLieu (@PathVariable Long idLop,@PathVariable Long idHocVien,@PathVariable Long idHoaDon,@PathVariable Long idThanhToan){
        LopHoc lop =lopHocService.findById(idLop).orElseThrow(() -> new RuntimeException("Lop hoc not found"));
        HocVien hocVien = hocVienService.findByIdHocVien(idHocVien).orElseThrow(() -> new RuntimeException("Hoc vien not found"));
        HoaDon hoaDon = hoaDonService.findById(idHoaDon).orElseThrow(() -> new RuntimeException("Hoa don not found"));
        ThanhToan thanhToan = thanhToanService.findById(idThanhToan).orElseThrow(() -> new RuntimeException("Thanh toan not found"));
        thanhToan.setNguoiThanhToan(hocVien);
        thanhToan.setHoaDon(hoaDon);
        thanhToan.setLopHoc(lop);
        thanhToan.setTrangThai(ThanhToanEnum.DONE);
        return thanhToanService.createThanhToan(thanhToan);
    }
    @Operation(
            summary = "update thanh toan cancel",
            description = """
           
             id thanhToaN
            Update ThanHtOAN cancel
    """
    )
    @GetMapping("/delete/{id}")
    public ThanhToan deleteKhoa (@PathVariable Long id){
        ThanhToan tt = thanhToanService.findById(id).orElseThrow(() -> new RuntimeException("Thanh toan not found"));
        tt.setTrangThai(ThanhToanEnum.CANCEL);
        return thanhToanService.createThanhToan(tt);
    }
    @GetMapping("/getAll")
    public List<ThanhToan> findAll (){
        return  thanhToanService.findAll();
    }
    @GetMapping("/findById/{id}")
    public ThanhToan findById (@PathVariable Long id){
        ThanhToan tt = thanhToanService.findById(id).orElseThrow(() -> new RuntimeException("Thanh toan not found"));
        return thanhToanService.createThanhToan(tt);
    }
    @GetMapping("/findByIdHV/{idHV}")
    public List<ThanhToan> findByIdHocVien (@PathVariable Long idHV){
        List<ThanhToan> list = thanhToanService.findByIdHV(idHV);
        return list;
    }
    @GetMapping("/findByIdHD/{idHD}")
    public List<ThanhToan> findByIdHoaDon (@PathVariable Long idHD){
        List<ThanhToan> list = thanhToanService.findByIdHoaDon(idHD);
        return list;
    }
    @GetMapping("/findByIdLop/{idLop}")
    public List<ThanhToan> findByIdLop (@PathVariable Long idLop){
        List<ThanhToan> list = thanhToanService.finfByIdLop(idLop);
        return list;
    }
    @Operation(
            summary = "get list thanh toan có idHV và enum wait",
            description = """
           
             id thanhToaN
            Update ThanHtOAN cancel
    """
    )
    @GetMapping("/findByIdLop/{idHV}")
    public List<ThanhToan> findByIdLopVaEnum (@PathVariable Long idHV){
        List<ThanhToan> list = thanhToanService.findByIDHVvaEnum(idHV, ThanhToanEnum.WAIT);
        return list;
    }
}
