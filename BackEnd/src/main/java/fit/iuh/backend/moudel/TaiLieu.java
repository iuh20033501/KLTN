package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiLieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTaiLieu;
    private String tenTaiLieu;
    private String noiDung;
    private String linkLoad;
    @OneToOne
    private BuoiHoc buoiHoc;


}
