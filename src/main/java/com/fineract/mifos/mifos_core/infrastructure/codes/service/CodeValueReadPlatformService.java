package com.fineract.mifos.mifos_core.infrastructure.codes.service;


import com.fineract.mifos.mifos_core.infrastructure.codes.dto.CodeValueData;

import java.util.Collection;

/**
 * A service for retrieving code value information based on the code itself.
 *
 * There are two types of code information in the platform:
 * <ol>
 * <li>System defined codes</li>
 * <li>User defined codes</li>
 * </ol>
 *
 * <p>
 * System defined codes cannot be altered or removed but their code values may be allowed to be added to or removed.
 * </p>
 *
 * <p>
 * User defined codes can be changed in any way by application users with system permissions.
 * </p>
 */
public interface CodeValueReadPlatformService {

    Collection<CodeValueData> retrieveCodeValuesByCode(String code);

    Collection<CodeValueData> retrieveAllCodeValues(Long codeId);

    CodeValueData retrieveCodeValue(Long codeValueId);

}
