package com.example.backend.controller;

import com.example.backend.Service.TaiKhoanService;
import com.example.backend.Service.UserService;
import com.example.backend.moudel.TaiKhoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class TaiKhoanController {
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private UserService userService;
    @GetMapping("/findByName/{id}")
    public TaiKhoan getByName(@PathVariable String tenTaiKhoan){
        return  taiKhoanService.findByTenDangNhap(tenTaiKhoan);
    }
    @PostMapping("/save")
    public TaiKhoan createAcount(@RequestBody TaiKhoan taiKhoan){
        return taiKhoanService.createTaiKhoan(taiKhoan);
    }
    @PutMapping("/update")
    public TaiKhoan updateTaiKhoan(@RequestBody TaiKhoan taiKhoan){
        return taiKhoanService.createTaiKhoan(taiKhoan);
    }
    @GetMapping("/login/{id}")
    public String Login(@PathVariable String tenTaiKhoan, @RequestParam String matKhau) {
        TaiKhoan taiKhoan = taiKhoanService.findByTenDangNhap(tenTaiKhoan);
        if (taiKhoan.getTenDangNhap() != null) {
            if (taiKhoan.getMatKhau().equals(matKhau)) {
                if (taiKhoan.getUser().getTrangThai().equals(true))
                    return userService.getRole(taiKhoan.getUser().getIdUser());
                else return "tài khoản bị khóa";
            } else return "sai mật khẩu";
        }
        return "không tìm thấy tài khoản";
    }



}
