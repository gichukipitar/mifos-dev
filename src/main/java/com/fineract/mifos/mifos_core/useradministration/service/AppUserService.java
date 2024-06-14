package com.fineract.mifos.mifos_core.useradministration.service;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.EnumOptionData;
import com.fineract.mifos.mifos_core.infrastructure.security.service.PlatformPasswordEncoder;
import com.fineract.mifos.mifos_core.infrastructure.security.service.RandomPasswordGenerator;
import com.fineract.mifos.mifos_provider.useradministration.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PlatformPasswordEncoder platformPasswordEncoder;
    @Autowired
    private RandomPasswordGenerator randomPasswordGenerator;

    public EnumOptionData organisationalRoleData() {
        EnumOptionData organisationalRole = null;
        if (this.staff != null) {
            organisationalRole = this.staff.organisationalRoleData();
        }
        return organisationalRole;
    }


}
