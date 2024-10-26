package fit.iuh.backend.controller;

import fit.iuh.backend.enumclass.LopEnum;
import fit.iuh.backend.moudel.*;
import fit.iuh.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
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
        lop.setTrangThai(LopEnum.DELETE);
        return lopHocService.createLopHoc(lop);
    }
    @Operation(
            summary = "set trg thái lớp Full",
            description = """ 
          khi lớp đầy học viên
    """
    )
    @GetMapping("/fullLop/{id}")
    public LopHoc fullLop(@PathVariable Long id){
        LopHoc lop = lopHocService.findById(id).get();
        lop.setTrangThai(LopEnum.FULL);
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
    public List<LopHoc> findByKhoa (@PathVariable Long id){
        return  lopHocService.findByKhoa(id);
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


}
