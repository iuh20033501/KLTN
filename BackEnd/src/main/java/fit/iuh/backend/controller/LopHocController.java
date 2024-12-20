package fit.iuh.backend.controller;

import fit.iuh.backend.dto.CreateLopDTO;
import fit.iuh.backend.enumclass.TrangThaiLop;
import fit.iuh.backend.moudel.*;
import fit.iuh.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @PostMapping("/createLopDestop/{idKhoa}/{idGV}")
    public ResponseEntity<LopHoc> createLopDestop(
            @RequestBody CreateLopDTO lopDTO,
            @PathVariable Long idKhoa,
            @PathVariable Long idGV) {
        System.out.println("Payload nhận được: " + lopDTO.getNgayBD());
        System.out.println("Payload nhận được: " + lopDTO.getNgayKT());
        Optional<GiangVien> gvOptional = gvService.findById(idGV);
        Optional<KhoaHoc> khoaOptional = khoaHocService.findById(idKhoa);

        if (gvOptional.isEmpty() || khoaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 nếu không tìm thấy giảng viên hoặc khóa học
        }

        GiangVien gv = gvOptional.get();
        KhoaHoc khoa = khoaOptional.get();
        LopHoc lop = lopDTO.getLop();

        try {
            // Định dạng chuỗi ngày tháng ISO 8601
            SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            if (lopDTO.getNgayBD() != null) {
                Date ngayBD = isoDateFormat.parse(lopDTO.getNgayBD());
                lop.setNgayBD(ngayBD);
            }
            if (lopDTO.getNgayKT() != null) {
                Date ngayKT = isoDateFormat.parse(lopDTO.getNgayKT());
                lop.setNgayKT(ngayKT);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 nếu định dạng ngày không hợp lệ
        }

        lop.setKhoaHoc(khoa);
        lop.setGiangVien(gv);

        LopHoc createdLopHoc = lopHocService.createLopHoc(lop);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLopHoc); // 201 khi tạo thành công
    }

    @GetMapping("/getLop/{id}")
    public LopHoc findByidLop (@PathVariable Long id){
        return lopHocService.findById(id).orElse(null);
    }
    @GetMapping("/getAll")
    public List<LopHoc> findAll (){
        return  lopHocService.findAll();
    }
    @GetMapping("/getAllTrue")
    public List<LopHoc> getAllTrue (){
        return  lopHocService.findAllTrue();
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
public ResponseEntity<LopHoc> deleteLop(@PathVariable Long id) {
    // Tìm lớp học theo ID
    LopHoc lop = lopHocService.findById(id)
            .orElseThrow(() -> new RuntimeException("Lớp học không tồn tại"));

    // Cập nhật trạng thái lớp học
    lop.setTrangThai(TrangThaiLop.DELETE);

    // Lưu lại đối tượng đã cập nhật
    LopHoc updatedLop = lopHocService.createLopHoc(lop);

    // Trả về phản hồi
    return ResponseEntity.ok(updatedLop);
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

    @GetMapping("/getLikeNameLop/{name}")
    public List<LopHoc> findLikeNameLop (@PathVariable String name){
        return lopHocService.findLikeName(name);
    }
    @GetMapping("/getLikeNameGV/{name}")
    public List<LopHoc> findLikeNameGV (@PathVariable String name){
        return lopHocService.findLikeNameGiangVien(name);
    }
    @GetMapping("/getLikeNameKhoa/{name}")
    public List<LopHoc> findLikeNameKhoa (@PathVariable String name){
        return lopHocService.findLikeNameKhoa(name);
    }



}
