package com.fineract.mifos.mifos_core.infrastructure.security.service;

import com.fineract.mifos.mifos_core.commands.domain.CommandWrapper;
import com.fineract.mifos.mifos_core.useradministration.entity.AppUser;
import org.springframework.stereotype.Component;

@Component
public interface PlatformSecurityContext extends PlatformUserRightsContext{
    AppUser authenticatedUser();

    /**
     * Convenience method returns null (does not throw an exception) if an authenticated user is not present
     *
     * To be used only in service layer methods that can be triggered via both the API and batch Jobs (which do not have
     * an authenticated user)
     *
     * @return
     */
    AppUser getAuthenticatedUserIfPresent();

    void validateAccessRights(String resourceOfficeHierarchy);

    String officeHierarchy();

    boolean doesPasswordHasToBeRenewed(AppUser currentUser);

    AppUser authenticatedUser(CommandWrapper commandWrapper);
}
