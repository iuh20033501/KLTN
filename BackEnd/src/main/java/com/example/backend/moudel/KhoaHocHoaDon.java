package com.example.backend.moudel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KhoaHocHoaDon {
    @Id
    @ManyToOne
    private KhoaHoc idKhoaHoc;
    @Id
    @ManyToOne
    private HoaDon  idHoaDon ;
}
