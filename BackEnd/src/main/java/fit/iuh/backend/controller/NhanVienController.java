package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.GiangVien;
import fit.iuh.backend.moudel.NhanVien;
import fit.iuh.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nhanVien")
public class NhanVienController {
    @Autowired
    private NhanVienService nhanVienService;
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

    @PutMapping("/update/{id}")
    public NhanVien updateHocVien(@PathVariable Long id, @RequestBody GiangVien giangVien){
        NhanVien f=nhanVienService.findById(id).orElseThrow(()->new RuntimeException("nhan vien not found "));
        f.setEmail(giangVien.getEmail());
        f.setDiaChi(giangVien.getDiaChi());
        f.setImage(giangVien.getImage());
        f.setSdt(giangVien.getSdt());
        f.setGioiTinh(giangVien.isGioiTinh());
        f.setHoTen(giangVien.getHoTen());
        f.setNgaySinh(giangVien.getNgaySinh());
        f.setLuongThang(giangVien.getLuong());
        return nhanVienService.createNhanVien(f);
    }
    //
    @GetMapping("/findbyId/{id}")
    public NhanVien findById(@PathVariable Long id){
        return nhanVienService.findById(id).orElse(null);
    }

    @GetMapping("/findByName/{name}")
    public NhanVien findByName(@PathVariable String name){
        return nhanVienService.findByName(name);
    }
    @GetMapping("/findAll")
    public List<NhanVien> findAll(){
        return nhanVienService.findAlL();
    }
}
