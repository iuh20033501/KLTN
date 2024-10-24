package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.BuoiHoc;
import fit.iuh.backend.moudel.LopHoc;
import fit.iuh.backend.service.BuoiHocService;
import fit.iuh.backend.service.LopHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/buọihoc")
public class BuoiHocController {
    @Autowired
    private BuoiHocService buoiHocService;
    @Autowired
    private LopHocService lopHocService;
    @PostMapping("/createBuoiHoc/{id}")
    public BuoiHoc createBuoiHoc(@PathVariable Long idLop, @RequestBody BuoiHoc buoiHoc){
      LopHoc lop= lopHocService.findById(idLop).get();
      buoiHoc.setLopHoc(lop);
        return buoiHocService.createBuoiHoc(buoiHoc);
    }
    @GetMapping("/getbuoiHocByLop/{idLop}")
    public List<BuoiHoc> getBuoiByLop(@PathVariable  Long idLop){
        return buoiHocService.getByIdLop(idLop);
    }
    @GetMapping("/getAll")
    public List<BuoiHoc> getAll(){
        return buoiHocService.getAll();
    }
    @PostMapping("/update/{id}")
    public BuoiHoc updateBuoiHoc(@PathVariable Long id ,@RequestBody BuoiHoc buoiHoc){
        buoiHoc.setIdBuoiHoc(id);
        return buoiHocService.createBuoiHoc(buoiHoc);
    }
    @GetMapping("/getByHocVien/{id}")
    public List<BuoiHoc> getByHocVien(@PathVariable Long id ){
        return buoiHocService.getBuoiByHocVien(id);
    }

}
