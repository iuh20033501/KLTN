package com.example.backend.dto;

import com.example.backend.moudel.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logindto {
    private  String trangThai;
    private User user;
}
