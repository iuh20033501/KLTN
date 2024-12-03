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
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/create/{idNhanVien}/{idHocVien}")
    public HoaDon createNhanVien (@PathVariable Long idNhanVien,@PathVariable Long idHocVien ,@RequestBody List<ThanhToan> list) {
        HoaDon hoaDon = new HoaDon();
        NhanVien nhanVien = nhanVienService.findById(idNhanVien).orElseThrow(()->new RuntimeException("nhan vien not found "));
        hoaDon.setNguoiLap(nhanVien);
        Date date = new Date();
        hoaDon.setNgayLap(date);
        Long tongTien = 0L;
        hoaDon.setThanhTien(tongTien);
        HoaDon hd= hoaDonService.createHoaDon(hoaDon);
        for(ThanhToan tt: list ){
            thanhToanService.updateDoneThanhToanAndIdHoaDon(tt,hd.getIdHoaDon());
            tongTien += tt.getLopHoc().getKhoaHoc().getGiaTien();
        }
        hd.setThanhTien(tongTien);

        return hoaDonService.createHoaDon(hd);
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
//    @GetMapping("/delete/{idHD}")
//    public HoaDon deleteHD(@PathVariable Long idHD){
//        return hoaDonService.deleteHoaDon(idHD);
//    }
}
