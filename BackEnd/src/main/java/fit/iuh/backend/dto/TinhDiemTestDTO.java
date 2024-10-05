package fit.iuh.backend.dto;

import fit.iuh.backend.moudel.CauTraLoi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TinhDiemTestDTO {
    private List<CauTraLoi> listCauTraLoi;
    private Long idBaiTest;
    private Long idHocVien;
}
