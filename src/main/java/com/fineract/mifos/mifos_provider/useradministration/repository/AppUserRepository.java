package com.fineract.mifos.mifos_provider.useradministration.repository;

import com.fineract.mifos.mifos_provider.infrastructure.security.repository.PlatformUserRepository;
import com.fineract.mifos.mifos_core.useradministration.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser>, PlatformUserRepository {
    @Query("select appUser from AppUser appUser where appUser.username = :username")
    AppUser findAppUserByName(@Param("username") String username);
    Collection<AppUser> findByOfficeId(Long officeId);
}
