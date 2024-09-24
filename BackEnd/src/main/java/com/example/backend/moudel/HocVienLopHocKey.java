package com.example.backend.moudel;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class HocVienLopHocKey implements Serializable {
    @ManyToOne
    private HocVien HocVien;
    @ManyToOne
    private LopHoc  LopHoc;

}
