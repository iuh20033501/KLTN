package fit.iuh.backend.controller;

import fit.iuh.backend.enumclass.ThanhToanEnum;
import fit.iuh.backend.moudel.*;
import fit.iuh.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @GetMapping("/create/{idLop}/{idHocVien}")
    public ThanhToan createThanhToan (@PathVariable Long idLop,@PathVariable Long idHocVien){
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
    public ThanhToan deleteThanhToan (@PathVariable Long id){
        ThanhToan tt = thanhToanService.findById(id).orElseThrow(() -> new RuntimeException("Thanh toan not found"));
        tt.setTrangThai(ThanhToanEnum.CANCEL);
        return thanhToanService.createThanhToan(tt);
    }
    @Operation(
            summary = "upload thanh toan ",
            description = """
          xet đièu kiện xem lớp học chưa bắt đàu, nếu đã bắt đầu xóa thành trạng thái 
          trả vè danh sách lớp đẫ thanh toán và chờ thanh toán hợp lệ
    """
    )
    @GetMapping("/upload/{idHV}")
    public List<ThanhToan> uploadThanhToan(@PathVariable Long idHV) {
        List<ThanhToan> list = thanhToanService.findByIDHVvaEnum(idHV, ThanhToanEnum.WAIT);
        Date currentDate = new Date();
        list.removeIf(tt -> {
            if (tt.getLopHoc().getNgayBD().before(currentDate)) {
                tt.setTrangThai(ThanhToanEnum.CANCEL);
                return true; // Xóa tt khỏi danh sách
            }
            return false;
        });
        List<ThanhToan> listDone = thanhToanService.findByIDHVvaEnum(idHV, ThanhToanEnum.DONE);
        list.addAll(listDone);

        return list;
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
    @Operation(
            summary = "getAll thanh toan dù có cancel hay không ",
            description = """
             id Lop
    """
    )
    @GetMapping("/findByIdLop/{idLop}")
    public List<ThanhToan> findByIdLop (@PathVariable Long idLop){
        List<ThanhToan> list = thanhToanService.finfByIdLop(idLop);
        return list;
    }
    @Operation(
            summary = "getAll thanh toan dù có trang thái là Done và wait",
            description = """
             truyền IdLop
             dung de get những thanh toán của lớp ẩn đi thanh toan cancel
             xét duyên cái thanh toán wait ngày bắt đàu mở lớp qua ngày hiện tại thì xóa
             id Lop
    """
    )
    @GetMapping("/findByIdLopNotCancel/{idLop}")
    public List<ThanhToan> findByIdLopNotCancel(@PathVariable Long idLop) {
        List<ThanhToan> listWait = thanhToanService.findByIdLopvaEnum(idLop, ThanhToanEnum.WAIT);
        List<ThanhToan> listDone = thanhToanService.findByIdLopvaEnum(idLop, ThanhToanEnum.DONE);
        thanhToanService.reLoadThanhToanByIdLop(idLop);
        listDone.addAll(listWait);
        return listDone;
    }
    @Operation(
            summary = "reLoad lại thanh toán wait",
            description = """
             truyền IdLop
             xét duyên cái thanh toán wait ngày bắt đàu mở lớp qua ngày hiện tại thì xóa
             
    """
    )
    @GetMapping("/reLoadTTByIdLop/{idLop}")
    public void reLoadByIdLop(@PathVariable Long idLop) {
        thanhToanService.reLoadThanhToanByIdLop(idLop);
    }

    @Operation(
            summary = "get list thanh toan có idHV và enum wait",
            description = """
           
             id thanhToaN
            Update ThanHtOAN cancel
    """
    )
    @GetMapping("/findByIdHocVienWait/{idHV}")
    public List<ThanhToan> findByIdLopVaEnum (@PathVariable Long idHV){
        List<ThanhToan> list = thanhToanService.findByIDHVvaEnum(idHV, ThanhToanEnum.WAIT);
        return list;
    }
}
