package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.KhoaHoc;
import fit.iuh.backend.moudel.LopHoc;
import fit.iuh.backend.service.KhoaHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/khoahoc")
public class KhoaHocController {
    @Autowired
    private KhoaHocService khoaHocService;

    @PostMapping("/create")
    public KhoaHoc createLop ( @RequestBody KhoaHoc khoa){
        khoa.setTrangThai(true);
        return khoaHocService.createKhoaHoc(khoa);
    }
    //
    @GetMapping("/getKhoa/{id}")
    public KhoaHoc findByidKhoa (@PathVariable Long idKhoa){
        Optional<KhoaHoc> k = khoaHocService.findById(idKhoa);
        if (k.isPresent()){
            return k.get();
        }
        return null;
    }
    //
    @GetMapping("/xoaKhoa/{id}")
    public KhoaHoc deleteKhoa (@PathVariable Long idKhoa){
        KhoaHoc khoaHoc = khoaHocService.findById(idKhoa).orElseThrow(() -> new RuntimeException("Khoa hoc not found"));
        khoaHoc.setTrangThai(false);
        return khoaHocService.createKhoaHoc(khoaHoc);
    }
    @GetMapping("/getAll")
    public List<KhoaHoc> findAll (){
        return  khoaHocService.getAll();
    }




}
