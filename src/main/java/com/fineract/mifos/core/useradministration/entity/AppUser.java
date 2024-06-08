package com.fineract.mifos.core.useradministration.entity;

import com.fineract.mifos.core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.core.infrastructure.core.domain.AbstractPersistableCustom;
import com.fineract.mifos.core.infrastructure.core.dto.EnumOptionData;
import com.fineract.mifos.core.infrastructure.core.service.DateUtils;
import com.fineract.mifos.core.infrastructure.security.exception.NoAuthorizationException;
import com.fineract.mifos.core.infrastructure.security.service.PlatformUser;
import com.fineract.mifos.core.infrastructure.security.service.RandomPasswordGenerator;
import com.fineract.mifos.core.organisation.office.entity.Office;
import com.fineract.mifos.core.organisation.staff.entity.Staff;
import com.fineract.mifos.core.portifolio.client.entity.Client;
import com.fineract.mifos.core.useradministration.service.AppUserConstants;
import jakarta.persistence.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "m_appuser", uniqueConstraints = @UniqueConstraint(columnNames = { "username" }, name = "username_org"))

public class AppUser extends AbstractPersistableCustom implements PlatformUser {
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "firstname", nullable = false, length = 100)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nonexpired", nullable = false)
    private boolean accountNonExpired;

    @Column(name = "nonlocked", nullable = false)
    private boolean accountNonLocked;

    @Column(name = "nonexpired_credentials", nullable = false)
    private boolean credentialsNonExpired;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "firsttime_login_remaining", nullable = false)
    private boolean firstTimeLoginRemaining;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false)
    private Office office;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = true)
    private Staff staff;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "m_appuser_role", joinColumns = @JoinColumn(name = "appuser_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Column(name = "last_time_password_updated")
    private LocalDate lastTimePasswordUpdated;

    @Column(name = "password_never_expires", nullable = false)
    private boolean passwordNeverExpires;

    @Column(name = "is_self_service_user", nullable = false)
    private boolean isSelfServiceUser;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "appUser")
    private Set<AppUserClientMapping> appUserClientMappings = new HashSet<>();

    @Column(name = "cannot_change_password", nullable = true)
    private Boolean cannotChangePassword;

     protected AppUser() {
        this.accountNonLocked = false;
        this.credentialsNonExpired = false;
        this.roles = new HashSet<>();
    }

    public AppUser(final Office office, final User user, final Set<Role> roles, final String email, final String firstname,
                   final String lastname, final Staff staff, final boolean passwordNeverExpire, final boolean isSelfServiceUser,
                   final Collection<Client> clients, final Boolean cannotChangePassword) {
        this.office = office;
        this.email = email.trim();
        this.username = user.getUsername().trim();
        this.firstname = firstname.trim();
        this.lastname = lastname.trim();
        this.password = user.getPassword().trim();
        this.accountNonExpired = user.isAccountNonExpired();
        this.accountNonLocked = user.isAccountNonLocked();
        this.credentialsNonExpired = user.isCredentialsNonExpired();
        this.enabled = user.isEnabled();
        this.roles = roles;
        this.firstTimeLoginRemaining = true;
        this.lastTimePasswordUpdated = DateUtils.getLocalDateOfTenant();
        this.staff = staff;
        this.passwordNeverExpires = passwordNeverExpire;
        this.isSelfServiceUser = isSelfServiceUser;
        this.appUserClientMappings = createAppUserClientMappings(clients);
        this.cannotChangePassword = cannotChangePassword;
    }








}
