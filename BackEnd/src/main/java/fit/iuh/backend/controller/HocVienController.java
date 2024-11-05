package fit.iuh.backend.controller;

import fit.iuh.backend.dto.ResetPassDto;
import fit.iuh.backend.dto.SigninDTO;
import fit.iuh.backend.dto.TaiKhoanDto;
import fit.iuh.backend.moudel.*;
import fit.iuh.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hocvien")
public class HocVienController {
    @Autowired
    private HocVienService hocVienService;
    @Qualifier("authServiceImpl")
    @Autowired
    private AuthService service;

    @Qualifier("taiKhoanImplement")
    @Autowired
    private TaiKhoanService tkService;

    @Autowired
    private UserService userService;
    @Autowired
    private LopHocService lopHocService;
    @Autowired
    private HocVienLopHocService hocVienLopHocService;
    //
    @PutMapping("/update/{id}")
    public HocVien updateHocVien( @PathVariable Long id,@RequestBody HocVien hocVien){
        HocVien f=hocVienService.findByIdHocVien(id).orElseThrow(()->new RuntimeException("hoc vien not found "));
        f.setEmail(hocVien.getEmail());
        f.setKiNangCan(hocVien.getKiNangCan());
        f.setDiaChi(hocVien.getDiaChi());
        f.setImage(hocVien.getImage());
        f.setSdt(hocVien.getSdt());
        f.setGioiTinh(hocVien.isGioiTinh());
        f.setHoTen(hocVien.getHoTen());
        f.setNgaySinh(hocVien.getNgaySinh());
        return hocVienService.createHocVien(f);
    }
    //
    @GetMapping("/findbyId/{id}")
    public HocVien findById(@PathVariable Long id){
        return hocVienService.findByIdHocVien(id).orElse(null);
    }

    @GetMapping("/findByName/{name}")
    public HocVien findByName(@PathVariable String name){
        return hocVienService.findByName(name);
    }
    @GetMapping("/findAll")
    public List<HocVien> findAll(){
        return hocVienService.getAll();
    }

    @Operation(
            summary = "Đăng ký lớp của học viên",
            description = """ 
            truyền idlop, idhocvien vao parm
            trước khi đăng kí cần chạy hàm get list hoc viên lấy số lượng so sánh đầy chưa
            nếu đầy thành viên đó nữa đầy thì dky xong chạy hàm setfull của lớp
    """
    )
    @GetMapping("/dangkyLop/{idLop}/{idHV}")
    public ResponseEntity<HocVienLopHoc> dangKyLop(@PathVariable Long idLop, @PathVariable Long idHV) {
        HocVienLopHocKey key = new HocVienLopHocKey() ;
        HocVien hv = hocVienService.findByIdHocVien(idHV).orElseThrow(()->new RuntimeException("hoc vien not found "));
        LopHoc lop = lopHocService.findById(idLop).orElseThrow(()->new RuntimeException("lop hoc not found "));
        key.setLopHoc(lop);
        key.setHocVien(hv);
        int siSo = hocVienLopHocService.findByidLop(key.getLopHoc().getIdLopHoc()).size();
        if(lop.getSoHocVien()<=siSo){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        HocVienLopHoc result = hocVienLopHocService.dangKyLopHoc(key);
        if (result != null) {
            return ResponseEntity.ok(result); // Trả về kết quả thành công
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Trả về lỗi nếu không thể đăng ký
        }
    }
    @Operation(
            summary = "Đăng ký lớp của học viên",
            description = """ 
            truyền idlop, idhocvien vao parm
           mà không xet điều kiển sỉ số lớp học
    """
    )
    @GetMapping("/dangkyLop2/{idLop}/{idHV}")
    public ResponseEntity<HocVienLopHoc> dangKyLop2(@PathVariable Long idLop, @PathVariable Long idHV) {
        HocVienLopHocKey key = new HocVienLopHocKey() ;
        HocVien hv = hocVienService.findByIdHocVien(idHV).orElseThrow(()->new RuntimeException("hoc vien not found "));
        LopHoc lop = lopHocService.findById(idLop).orElseThrow(()->new RuntimeException("lop hoc not found "));
        key.setLopHoc(lop);
        key.setHocVien(hv);
//        int siSo = hocVienLopHocService.findByidLop(key.getLopHoc().getIdLopHoc()).size();
//        if(lop.getSoHocVien()<=siSo){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
        HocVienLopHoc result = hocVienLopHocService.dangKyLopHoc(key);
        if (result != null) {
            return ResponseEntity.ok(result); // Trả về kết quả thành công
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Trả về lỗi nếu không thể đăng ký
        }
    }
    @Operation(
            summary = "lấy lớp tù học viên",
            description = """ 
           truyền id học viên
    """
    )
    @GetMapping("/getByHV/{id}")
    public List<LopHoc> findByHocVIEN (@PathVariable Long id){
        List<HocVienLopHoc> listHVLH = hocVienLopHocService.findByIdHocVien(id);
        List<LopHoc> list = new ArrayList<>();
        for (HocVienLopHoc hvlh : listHVLH) {
            list.add(hvlh.getKey().getLopHoc());
        }
        return list;
    }



}
