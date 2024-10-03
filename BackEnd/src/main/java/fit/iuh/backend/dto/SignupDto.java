package fit.iuh.backend.dto;

//import jakarta.validation.constraints.*;
import fit.iuh.backend.enumclass.SkillEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
//    @NotNull(message = "Mã số sinh viên là bắt buộc")
//    @Pattern(regexp = "^\\d{8}$", message = "Mã số sinh viên phải gồm 8 số")
    private String username;
//    @NotNull(message = "Tên là bắt buộc")
//    @Pattern(regexp = "^([A-ZÀÁẢẠÃĂẰẮẲẶẴÂẦẤẨẬẪĐEÈÉẺẸẼÊỀẾỂỆỄIÌÍỈỊĨOÒÓỎỌÕÔỒỐỔỘỖƠỜỚỞỢỠUÙÚỦỤŨƯỪỨỬỰỮYỲÝỶỴỸa-zàáảạãăằắẳặẵâầấẩậẫđeèéẻẹẽêềếểệễiìíỉịĩoòóỏọõôồốổộỗơờớởợỡuùúủụũưừứửựữyỳýỷỵỹ\\s?])+$", message = "Tên phải có 2 ký tự trở lên")
    private String name;
    private String email;
//    @NotNull(message = "password là bắt buộc")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&+=])(?=\\S+$).{8,32}$", message = "Mật khẩu từ 8 - 32 ký tự gồm tối thiểu 1 chữ cái viết hoa, 1 chữ cái viết thường, 1 chữ số và 1 ký tự đặc biệt")
    private String password;
    private String address;
    private String image;
    private String coverImage;
    private boolean gender;
    @NotNull(message = "số điện thoại là bắt buộc")
    private String phone;
    private LocalDate birthday;
    private String dto;
    private List<SkillEnum> ListKiNang;
    private Long luong;
    public SignupDto(String username, String name, String email, String password, String address, String image, String coverImage, boolean gender, String phone, LocalDate birthday, String dto, Long luong) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.image = image;
        this.coverImage = coverImage;
        this.gender = gender;
        this.phone = phone;
        this.birthday = birthday;
        this.dto = dto;
        this.luong = luong;
    }



    public SignupDto(String username, String name, String email, String password, String address, String image, String coverImage, boolean gender, String phone, LocalDate birthday, String dto, List<SkillEnum> listKiNang) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.image = image;
        this.coverImage = coverImage;
        this.gender = gender;
        this.phone = phone;
        this.birthday = birthday;
        this.dto = dto;
        ListKiNang = listKiNang;
    }
}




