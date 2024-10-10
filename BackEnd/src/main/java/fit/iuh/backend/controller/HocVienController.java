package fit.iuh.backend.controller;

import fit.iuh.backend.dto.ResetPassDto;
import fit.iuh.backend.dto.SigninDTO;
import fit.iuh.backend.dto.TaiKhoanDto;
import fit.iuh.backend.moudel.TaiKhoanLogin;
import fit.iuh.backend.moudel.User;
import fit.iuh.backend.service.AuthService;
import fit.iuh.backend.service.HocVienService;
import fit.iuh.backend.moudel.HocVien;
import fit.iuh.backend.service.TaiKhoanService;
import fit.iuh.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/update/{id}")
    private HocVien updateHocVien( @PathVariable Long id,@RequestBody HocVien hocVien){
        hocVien.setIdUser(id);
        return hocVienService.createHocVien( hocVien);
    }
    @GetMapping("/findbyId/{id}")
    private HocVien findById(@PathVariable Long id){
        return hocVienService.findByIdHocVien(id);
    }

    @GetMapping("/findByName/{name}")
    private HocVien findByName(@PathVariable String name){
        return hocVienService.findByName(name);
    }
    @GetMapping("/findAll")
    private List<HocVien> findAll(){
        return hocVienService.getAll();
    }




}
