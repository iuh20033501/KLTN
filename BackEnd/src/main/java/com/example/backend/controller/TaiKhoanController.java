package com.example.backend.controller;

import com.example.backend.Service.TaiKhoanService;
import com.example.backend.moudel.TaiKhoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/taikhoan")
public class TaiKhoanController {
    @Autowired
    private TaiKhoanService taiKhoanService;

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
        return taiKhoanService.updateTaiKhoan(taiKhoan);
    }
}
