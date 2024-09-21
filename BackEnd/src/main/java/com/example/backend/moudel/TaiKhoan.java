package com.example.backend.moudel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan {
    @Id
    private String tenDangNhap;
    @NotNull
    private String matKhau;
    @OneToOne
    private User User;
}
