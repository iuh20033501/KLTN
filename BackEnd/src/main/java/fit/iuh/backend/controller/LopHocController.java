package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.BuoiHoc;
import fit.iuh.backend.moudel.HocVienLopHoc;
import fit.iuh.backend.moudel.KhoaHoc;
import fit.iuh.backend.moudel.LopHoc;
import fit.iuh.backend.service.BuoiHocService;
import fit.iuh.backend.service.HocVienLopHocService;
import fit.iuh.backend.service.LopHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/lopHoc")
public class LopHocController {
    @Autowired
    private LopHocService lopHocService;
    @Autowired
    private HocVienLopHocService hocVienLopHocService;

    @PostMapping("/create")
    private LopHoc createLop (@RequestBody LopHoc lopHoc){
        return lopHocService.createLopHoc(lopHoc);
    }
    @GetMapping("/getLop/{id}")
    private LopHoc findByidLop (@PathVariable Long id){
        return lopHocService.findById(id).orElse(null);
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
    //
    @GetMapping("/getByHV/{id}")
    private List<LopHoc> findByHocVIEN (@PathVariable Long id){
        List<HocVienLopHoc> listHVLH = hocVienLopHocService.findByIdHocVien(id);
        List<LopHoc> list = new ArrayList<>();
        for (HocVienLopHoc hvlh : listHVLH) {
            list.add(hvlh.getKey().getLopHoc());
        }
        return list;
    }


}
