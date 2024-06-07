package com.fineract.mifos.core.infrastructure.security.service;

import com.fineract.mifos.core.commands.domain.CommandWrapper;

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
