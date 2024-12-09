package fit.iuh.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fit.iuh.backend.moudel.BuoiHoc;
import fit.iuh.backend.moudel.LopHoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateBuoiDTO {
    @JsonProperty("NgayHoc")
    private String NgayHoc;
    private BuoiHoc buoiHoc;
}
