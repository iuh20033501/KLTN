package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.BuoiHoc;
import fit.iuh.backend.moudel.KhoaHoc;
import fit.iuh.backend.moudel.TaiLieu;
import fit.iuh.backend.repository.TaiLieuRepo;
import fit.iuh.backend.service.BaiTapService;
import fit.iuh.backend.service.BuoiHocService;
import fit.iuh.backend.service.TaiLieuService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/taiLieu")
public class TaiLieuController {
    @Autowired
    private TaiLieuService taiLieuService;
    @Qualifier("buoiHocImplement")
    @Autowired
    private BuoiHocService buoiHoTapService;
    @Operation(
            summary = "Thêm tài liệu",
            description = """ 
            truyền tên tài liệu , linkload, noi dung,ngay mo ngày dóng
            id Buoi
    """
    )
    @PostMapping("/create/{idBuoi}")
    public TaiLieu createTaiLieu (@PathVariable Long idBuoi,@RequestBody TaiLieu taiLieu){
        BuoiHoc bh= buoiHoTapService.findById(idBuoi);
        taiLieu.setTrangThai(true);
        taiLieu.setBuoiHoc(bh);
        return taiLieuService.createTaiLieu(taiLieu);
    }
    @Operation(
            summary = "update tài liệu",
            description = """ 
            truyền tên tài liệu , linkload, noi dung,ngay mo ngày dóng,trang thái
            idBuoi, idTaiLieu muốn update
    """
    )
    @PostMapping("/update/{idBuoi}/{idTaiLieu}")
    public TaiLieu updateTaiLieu (@PathVariable Long idBuoi,@PathVariable Long idTaiLieu,@RequestBody TaiLieu taiLieu){
        BuoiHoc bh= buoiHoTapService.findById(idBuoi);
        taiLieu.setIdTaiLieu(idTaiLieu);
        taiLieu.setBuoiHoc(bh);
        return taiLieuService.createTaiLieu(taiLieu);
    }
    @GetMapping("/getKhoa/{id}")
    public TaiLieu findByid (@PathVariable Long id){
      Optional<TaiLieu> tl = taiLieuService.findById(id);
        if (tl.isPresent()){
            return tl.get();
        }
        return null;
    }
    //
    @GetMapping("/xoaKhoa/{id}")
    public TaiLieu deleteKhoa (@PathVariable Long id){
        TaiLieu tl = taiLieuService.findById(id).orElseThrow(() -> new RuntimeException("Hoc vien not found"));
        tl.setTrangThai(false);
        return taiLieuService.createTaiLieu(tl);
    }
    @GetMapping("/getAll")
    public List<TaiLieu> findAll (){
        return  taiLieuService.finfAll();
    }
    @Operation(
            summary = "kiem tra bài tai liệu ",
            description = """ 
            truyền id tài liẹu kiểm tra còn ở tỏng thơi gian mở không nếu còn thì vẫn giữ trang thái cũ nếu hết thì ẩn
    """
    )
    @GetMapping("/kiemtraDate/{id}")
    public TaiLieu kiemTra (@PathVariable Long id ){
        TaiLieu tl = taiLieuService.findById(id).orElseThrow(() -> new RuntimeException("Hoc vien khong tim thay"));
        Date ngay =new Date();
        if(tl.getNgayMo().after(ngay) && tl.getNgayDong().before(ngay) ) {
            tl.setTrangThai(false);
        }
        return taiLieuService.createTaiLieu(tl);
    }
}