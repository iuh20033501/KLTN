package fit.iuh.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fit.iuh.backend.moudel.LopHoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLopDTO {

    @JsonProperty("NgayBD")
    private String NgayBD;
    @JsonProperty("NgayKT")
    private String NgayKT;
    private LopHoc lop;

}
