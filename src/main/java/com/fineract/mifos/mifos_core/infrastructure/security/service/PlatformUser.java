package com.fineract.mifos.mifos_core.infrastructure.security.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interface to protect platform from implementation detail of spring security.
 */
public interface PlatformUser extends UserDetails {

}
