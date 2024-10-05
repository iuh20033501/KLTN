package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CauTraLoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCauTraLoi;
    private String noiDung;
    private Boolean ketQua;
    @ManyToOne
    private CauHoi cauHoi;
}
