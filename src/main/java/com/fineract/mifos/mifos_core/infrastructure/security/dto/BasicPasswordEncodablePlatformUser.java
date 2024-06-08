package com.fineract.mifos.mifos_core.infrastructure.security.dto;

import com.fineract.mifos.mifos_core.infrastructure.security.service.PlatformUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BasicPasswordEncodablePlatformUser implements PlatformUser {

    private Long id;
    private String username;
    private String password;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
