package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.HoaDon;
import fit.iuh.backend.moudel.HocVien;
import fit.iuh.backend.moudel.NhanVien;
import fit.iuh.backend.moudel.ThanhToan;
import fit.iuh.backend.service.HoaDonService;
import fit.iuh.backend.service.LopHocService;
import fit.iuh.backend.service.NhanVienService;
import fit.iuh.backend.service.ThanhToanService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hoaDon")
public class HoaDonController {
    @Autowired
    private ThanhToanService thanhToanService;
    //    @Qualifier("buoiHocImplement")
    @Autowired
    private NhanVienService nhanVienService;
    @Autowired
    private HoaDonService hoaDonService;

    @Operation(
            summary = "Thêm hoa don",
            description = """
           
            idnhanVien, idHocVien
            tự đôgn tinh tiền và set id Hoa Don
            tự động tạo hoa don ,tinh tien 
    """
    )
    @PostMapping("/create/{idNhanVien}")
    public ResponseEntity<HoaDon> createHoaDon(@PathVariable Long idNhanVien, @RequestBody ArrayList<Long> listIdThanhToan) {
        // Tìm nhân viên theo ID
        NhanVien nhanVien = nhanVienService.findById(idNhanVien)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại với ID: " + idNhanVien));

        // Khởi tạo hóa đơn mới
        HoaDon hoaDon = new HoaDon();
        hoaDon.setNguoiLap(nhanVien);
        hoaDon.setNgayLap(new Date());
        hoaDon.setThanhTien(0L); // Khởi tạo tổng tiền bằng 0

        // Lưu hóa đơn để lấy ID
        HoaDon hoaDonSaved = hoaDonService.createHoaDon(hoaDon);

        // Tính tổng tiền và cập nhật các mục thanh toán
        Long tongTien = 0L;
        for (Long idTT : listIdThanhToan) {
            ThanhToan tt= thanhToanService.findById(idTT).get();
            if (tt != null && tt.getLopHoc() != null && tt.getLopHoc().getKhoaHoc() != null) {
                Long giaTien = tt.getLopHoc().getKhoaHoc().getGiaTien();
                tongTien += (giaTien != null ? giaTien : 0); // Tránh lỗi nếu giá tiền null
                // Cập nhật trạng thái và ID hóa đơn cho mục thanh toán
                thanhToanService.updateDoneThanhToanAndIdHoaDon(tt, hoaDonSaved.getIdHoaDon());
            }
        }

        // Cập nhật tổng tiền vào hóa đơn
        hoaDonSaved.setThanhTien(tongTien);
        HoaDon finalHoaDon = hoaDonService.createHoaDon(hoaDonSaved); // Cập nhật lại hóa đơn

        // Trả về kết quả
        return ResponseEntity.ok(finalHoaDon);
    }


    @GetMapping("/getAll")
    public List<HoaDon> getAllHoaDon (){
        return  hoaDonService.findAll();

    }
    @GetMapping("/getByIdNhanVien/{idNhanVien}")
    public List<HoaDon> getHoaDonByNhanVien(@PathVariable Long idNhanVien){
        return hoaDonService.finfByIdNhanVien(idNhanVien);
    }
    @GetMapping("/getByIdHocVien/{idHocVien}")
    public List<HoaDon> getHoaDonByHocVien(@PathVariable Long idHocVien){
        return hoaDonService.finfByIdHocVien(idHocVien);
    }
    @GetMapping("/getByIdLop/{idLop}")
    public List<HoaDon> getHoaDonByLop(@PathVariable Long idLop){
        return hoaDonService.finfByIdLop(idLop);
    }
    @GetMapping("/getById/{idHD}")
    public HoaDon getHoaDonByID(@PathVariable Long idHD){
        return hoaDonService.findById(idHD).get();
    }
//    @GetMapping("/createHDAndTT/{idNhanVien}/{idHocVien}")
//    public HoaDon deleteHD(@PathVariable Long idHD,@RequestBody List<ThanhToan> listThanhToan){
//
//        return hoaDonService.(idHD);
//    }
}
