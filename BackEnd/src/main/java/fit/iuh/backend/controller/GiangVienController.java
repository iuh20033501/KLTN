package fit.iuh.backend.controller;

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
@Controller
@RequestMapping("/giangVien")
public class GiangVienController {
    @Autowired
    private GiangVienService giangVienService;
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
//    @Autowired
//    private HocVienLopHocService hocVienLopHocService;
//    //
    @PutMapping("/update/{id}")
    public GiangVien updateHocVien(@PathVariable Long id, @RequestBody GiangVien giangVien){
        GiangVien f=giangVienService.findById(id).orElseThrow(()->new RuntimeException("giang vien not found "));
        f.setEmail(giangVien.getEmail());
        f.setChuyenMon(giangVien.getChuyenMon());
        f.setDiaChi(giangVien.getDiaChi());
        f.setImage(giangVien.getImage());
        f.setSdt(giangVien.getSdt());
        f.setGioiTinh(giangVien.isGioiTinh());
        f.setHoTen(giangVien.getHoTen());
        f.setNgaySinh(giangVien.getNgaySinh());
        f.setLuong(giangVien.getLuong());
        return giangVienService.createGiangVien(f);
    }
    //
    @GetMapping("/findbyId/{id}")
    public GiangVien findById(@PathVariable Long id){
        return giangVienService.findById(id).orElse(null);
    }

    @GetMapping("/findByName/{name}")
    public GiangVien findByName(@PathVariable String name){
        return giangVienService.findByName(name);
    }
    @GetMapping("/findAll")
    public List<GiangVien> findAll(){
        return giangVienService.findAll();
    }
    

}
