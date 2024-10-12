package fit.iuh.backend.dto;


import fit.iuh.backend.enumclass.ChucVuEnum;
import fit.iuh.backend.moudel.TaiKhoanLogin;
import fit.iuh.backend.moudel.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanDto implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Boolean enable;
    @OneToOne
    private User user;
    @Enumerated(value = EnumType.STRING)
    private ChucVuEnum role;


    public TaiKhoanDto(TaiKhoanLogin tk) {
        this.id = tk.getId();
        this.username = tk.getTenDangNhap();
        this.password = tk.getMatKhau();
        this.enable = tk.getEnable();
        this.user = tk.getUser();
        this.role= tk.getRole();
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of( new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return enable.equals(true);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !enable.equals(false);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enable.equals(true);
    }

    @Override
    public boolean isEnabled() {
        return enable.equals(true);
    }
}
