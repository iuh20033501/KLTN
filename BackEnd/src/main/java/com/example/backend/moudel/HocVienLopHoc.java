package com.example.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HocVienLopHoc {
    @Id
    @ManyToOne
    private HocVien idHocVien;
    @Id
    @ManyToOne
    private LopHoc  idLop;
}
