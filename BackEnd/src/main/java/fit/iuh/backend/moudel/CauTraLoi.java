package fit.iuh.backend.moudel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}
