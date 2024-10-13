package fit.iuh.backend.controller;

import fit.iuh.backend.dto.ResetPassDto;
import fit.iuh.backend.dto.SigninDTO;
import fit.iuh.backend.dto.TaiKhoanDto;
import fit.iuh.backend.moudel.*;
import fit.iuh.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private HocVienLopHocService hocVienLopHocService;
    //
    @PutMapping("/update/{id}")
    private HocVien updateHocVien( @PathVariable Long id,@RequestBody HocVien hocVien){
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
    private HocVien findById(@PathVariable Long id){
        return hocVienService.findByIdHocVien(id).orElse(null);
    }

    @GetMapping("/findByName/{name}")
    private HocVien findByName(@PathVariable String name){
        return hocVienService.findByName(name);
    }
    @GetMapping("/findAll")
    private List<HocVien> findAll(){
        return hocVienService.getAll();
    }

    @GetMapping("/getByLop/{id}")
    private List<HocVien> findByHocVIEN (@PathVariable Long id){
        List<HocVienLopHoc> listHVLH = hocVienLopHocService.findByidLop(id);
        List<HocVien> list = new ArrayList<>();
        for (HocVienLopHoc hvlh : listHVLH) {
            list.add(hvlh.getKey().getHocVien());
        }
        return list;
    }



}
