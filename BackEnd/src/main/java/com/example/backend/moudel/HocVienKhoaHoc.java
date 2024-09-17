package com.example.backend.moudel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HocVienKhoaHoc {
    @Id
    @ManyToOne
    private HocVien idHocVien;
    @Id
    @ManyToOne
    private KhoaHoc  idKhoaHoc;
}
