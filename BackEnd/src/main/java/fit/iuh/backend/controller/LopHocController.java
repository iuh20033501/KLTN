package fit.iuh.backend.controller;

import fit.iuh.backend.enumclass.TrangThaiLop;
import fit.iuh.backend.moudel.*;
import fit.iuh.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lopHoc")
public class LopHocController {
    @Autowired
    private LopHocService lopHocService;
    @Autowired
    private KhoaHocService khoaHocService;
    @Qualifier("giangVienImplement")
    @Autowired
    private GiangVienService gvService;
    @Autowired
    private HocVienLopHocService hocVienLopHocService;
    @Operation(
            summary = "Thêm lớp",
            description = """ 
            truyền số lương học viên, tên lớp
            idKhoa ,id GiaoVien phia tren param
    """
    )
    @PostMapping("/create/{idKhoa}/{idGV}")
    public ResponseEntity<LopHoc> createLop(@RequestBody LopHoc lopHoc, @PathVariable Long idKhoa, @PathVariable Long idGV) {
        Optional<GiangVien> gvOptional = gvService.findById(idGV);
        Optional<KhoaHoc> khoaOptional = khoaHocService.findById(idKhoa);

        if (gvOptional.isEmpty() || khoaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Trả về 404 nếu không tìm thấy giảng viên hoặc khóa học
        }

        GiangVien gv = gvOptional.get();
        KhoaHoc khoa = khoaOptional.get();
        lopHoc.setKhoaHoc(khoa);
        lopHoc.setGiangVien(gv);
        lopHoc.setTrangThai(TrangThaiLop.READY);
        LopHoc createdLopHoc = lopHocService.createLopHoc(lopHoc);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLopHoc); // Trả về 201 khi tạo thành công
    }
    @Operation(
            summary = "Update lớp",
            description = """ 
            truyền số lương học viên, tên lớp
            idKhoa ,id GiaoVien,idLop phia tren param
    """
    )
    @PostMapping("/create/{idKhoa}/{idGV}/{idLop}")
    public ResponseEntity<LopHoc> createLop(@RequestBody LopHoc lopHoc, @PathVariable Long idKhoa, @PathVariable Long idGV,@PathVariable Long idLop) {
        Optional<GiangVien> gvOptional = gvService.findById(idGV);
        Optional<KhoaHoc> khoaOptional = khoaHocService.findById(idKhoa);

        if (gvOptional.isEmpty() || khoaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Trả về 404 nếu không tìm thấy giảng viên hoặc khóa học
        }

        GiangVien gv = gvOptional.get();
        KhoaHoc khoa = khoaOptional.get();
        lopHoc.setKhoaHoc(khoa);
        lopHoc.setGiangVien(gv);
        lopHoc.setIdLopHoc(idLop);

        LopHoc createdLopHoc = lopHocService.createLopHoc(lopHoc);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLopHoc); // Trả về 201 khi tạo thành công
    }

    @GetMapping("/getLop/{id}")
    public LopHoc findByidLop (@PathVariable Long id){
        return lopHocService.findById(id).orElse(null);
    }
    @GetMapping("/getAll")
    public List<LopHoc> findAll (){
        return  lopHocService.findAll();
    }
//    @Operation(
//            summary = "update lớp",
//            description = """
//          dièn đầy đủ thông tin lớp
//    """
//    )
//    @PutMapping("/update/{id}")
//    public LopHoc updateLop(@PathVariable Long id, @RequestBody LopHoc lop){
//        lop.setIdLopHoc(id);
//        return lopHocService.createLopHoc(lop);
//    }
    @GetMapping("/delete/{id}")
    public LopHoc updateLop(@PathVariable Long id){
        LopHoc lop = lopHocService.findById(id).get();
        lop.setTrangThai(TrangThaiLop.DELETE);
        return lopHocService.createLopHoc(lop);
    }
    @Operation(
            summary = "kiem tra date lop",
            description = """ 
           
            truyen id Lop  nếu quá ngay hay chưa tới sẽ ko hiện 
    """
    )
    @GetMapping("/kiemtraDate/{id}")
    public LopHoc kiemTra (@PathVariable Long id ){
        LopHoc lop = lopHocService.findById(id).orElseThrow(()->new RuntimeException("lop hoc not found "));
        Date ngay =new Date();
        if(lop.getNgayBD().after(ngay) && lop.getNgayKT().before(ngay) ) {
            lop.setTrangThai(TrangThaiLop.DELETE);
        }
        return lopHocService.createLopHoc(lop);
    }
    @Operation(
            summary = "set trg thái lớp Full",
            description = """ 
          xet dieu kien neu thanh vien dang ky da du => thanh vien trong lớp sẽ set full lớp
    """
    )
    @GetMapping("/fullLop/{id}")
    public LopHoc fullLop(@PathVariable Long id){
        Long soLuong = (long) hocVienLopHocService.findByidLop(id).size();

        LopHoc lop = lopHocService.findById(id).get();
        if(lop.getSoHocVien()<=soLuong) {
            lop.setTrangThai(TrangThaiLop.FULL);
            return lopHocService.createLopHoc(lop);
        }
        return lopHocService.createLopHoc(lop);
    }
    @Operation(
            summary = "lấy  lớp từ id gv ",
            description = """ 
           truyền id gv
    """
    )
    @GetMapping("/getByGv/{id}")
    public List<LopHoc> findByGv (@PathVariable Long id){
        return  lopHocService.findByGiangVien(id);
    }
    @Operation(
            summary = "lấy  lớp từ id khoa ",
            description = """ 
           truyền id khoa
    """
    )
    @GetMapping("/getByKhoa/{id}")
    public ResponseEntity<List<LopHoc>> getByKhoa(@PathVariable Long id) {
        List<LopHoc> lopHocs = lopHocService.findByKhoa(id);
        return ResponseEntity.ok(lopHocs);
    }


    @Operation(
            summary = "lấy  học viên  từ lớp ",
            description = """ 
           truyền id lớp học
    """
    )
    @GetMapping("/getByLop/{id}")
    public List<HocVien> findByHocVIEN (@PathVariable Long id){
        List<HocVienLopHoc> listHVLH = hocVienLopHocService.findByidLop(id);
        List<HocVien> list = new ArrayList<>();
        for (HocVienLopHoc hvlh : listHVLH) {
            list.add(hvlh.getKey().getHocVien());
        }
        return list;
    }
//    @GetMapping("/getAll")
//    public List<LopHoc> findAllLop (){
//        List<LopHoc> list = lopHocService.findAll();
//        return list;
//    }


}
