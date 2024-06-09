package com.fineract.mifos.mifos_core.useradministration.repository;

import com.fineract.mifos.mifos_core.useradministration.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}
