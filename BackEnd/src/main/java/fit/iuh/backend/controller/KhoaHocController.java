package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.KhoaHoc;
import fit.iuh.backend.moudel.LopHoc;
import fit.iuh.backend.service.KhoaHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/khoahoc")
public class KhoaHocController {
    @Autowired
    private KhoaHocService khoaHocService;

    @PostMapping("/create")
    private KhoaHoc createLop ( @RequestBody KhoaHoc khoa){
        return khoaHocService.createKhoaHoc(khoa);
    }
    //
    @GetMapping("/getKhoa/{id}")
    private KhoaHoc findByidKhoa (@PathVariable Long idKhoa){
        Optional<KhoaHoc> k = khoaHocService.findById(idKhoa);
        if (k.isPresent()){
            return k.get();
        }
        return null;
    }
    //
    @GetMapping("/xoaKhoa/{id}")
    private KhoaHoc deleteKhoa (@PathVariable Long idKhoa){
        KhoaHoc khoaHoc = khoaHocService.findById(idKhoa).orElseThrow(() -> new RuntimeException("Hoc vien not found"));
        khoaHoc.setTrangThai(false);
        return khoaHocService.createKhoaHoc(khoaHoc);
    }
    @GetMapping("/getAll")
    private List<KhoaHoc> findAll (){
        return  khoaHocService.getAll();
    }




}
