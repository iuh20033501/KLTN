package fit.iuh.backend.moudel;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long idUser;
    private String hoTen;
    private String sdt;
    private String diaChi;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngaySinh;
    private boolean gioiTinh;
    @Column(length = 1000000000)
    private String image;

}
