package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.BuoiHoc;
import fit.iuh.backend.moudel.KhoaHoc;
import fit.iuh.backend.moudel.LopHoc;
import fit.iuh.backend.service.BuoiHocService;
import fit.iuh.backend.service.LopHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lopHoc")
public class LopHocController {
    @Autowired
    private LopHocService lopHocService;

    @PostMapping("/create")
    private LopHoc createLop (@RequestBody LopHoc lopHoc){
        return lopHocService.createLopHoc(lopHoc);
    }
    @GetMapping("/getLop/{id}")
    private LopHoc findByidLop (@PathVariable Long id){
        return lopHocService.findById(id).get();
    }
    @GetMapping("/getAll")
    private List<LopHoc> findAll (){
        return  lopHocService.findAll();
    }

    @PutMapping("/update/{id}")
    private LopHoc updateLop(@PathVariable Long id, @RequestBody LopHoc lop){
        lop.setIdLopHoc(id);
        return lopHocService.createLopHoc(lop);
    }
    @GetMapping("/getByGv/{id}")
    private List<LopHoc> findByGv (@PathVariable Long id){
        return  lopHocService.findByGiangVien(id);
    }
    @GetMapping("/getByKhoa/{id}")
    private List<LopHoc> findByKhoa (@PathVariable Long id){
        return  lopHocService.findByKhoa(id);
    }



}