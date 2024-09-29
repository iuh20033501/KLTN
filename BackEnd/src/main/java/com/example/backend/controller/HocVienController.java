package com.example.backend.controller;

import com.example.backend.Service.HocVienService;
import com.example.backend.moudel.HocVien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hocvien")
public class HocVienController {
    @Autowired
    private HocVienService hocVienService;
    @PutMapping("/update/")
    private HocVien updateHocVien(@RequestBody HocVien hocVien){
        return hocVienService.createHocVien(hocVien);
    }
    @GetMapping("/findbyId/{id}")
    private HocVien findById(@PathVariable Long id){
        return hocVienService.findByIdHocVien(id).get();
    }
    @GetMapping("/findByName/{name}")
    private HocVien findByName(@PathVariable String name){
        return hocVienService.findByName(name);
    }

}
