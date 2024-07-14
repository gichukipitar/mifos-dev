package com.fineract.mifos.mifos_core.infrastructure.core.service.database;

import com.fineract.mifos.mifos_core.infrastructure.core.config.FineractProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DatabasePasswordEncryptor implements PasswordEncryptor {

    public static final String DEFAULT_ENCRYPTION = "AES/CBC/PKCS5Padding";

    private final FineractProperties fineractProperties;

    @SuppressWarnings("checkstyle:regexpsinglelinejava")
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    "Usage: java -cp fineract-provider.jar java -Dloader.main=org.apache.fineract.infrastructure.core.service.database.DatabasePasswordEncryptor org.springframework.boot.loader.PropertiesLauncher <masterPassword> <plainPassword>");
            System.exit(1);
        }
        String masterPassword = args[0];
        String plainPassword = args[1];
        String encryptedPassword = EncryptionUtil.encryptToBase64(DEFAULT_ENCRYPTION, masterPassword, plainPassword);
        System.out.println(MessageFormat.format("The encrypted password: {0}", encryptedPassword));
    }

    @Override
    public String encrypt(String plainPassword) {
        String masterPassword = Optional.ofNullable(fineractProperties.getTenant())
                .map(FineractProperties.FineractTenantProperties::getMasterPassword)
                .orElse(fineractProperties.getDatabase().getDefaultMasterPassword());
        String encryption = Optional.ofNullable(fineractProperties.getTenant())
                .map(FineractProperties.FineractTenantProperties::getEncryption).orElse(DEFAULT_ENCRYPTION);
        return EncryptionUtil.encryptToBase64(encryption, masterPassword, plainPassword);
    }

    @Override
    public String decrypt(String encryptedPassword) {
        String masterPassword = Optional.ofNullable(fineractProperties.getTenant())
                .map(FineractProperties.FineractTenantProperties::getMasterPassword)
                .orElse(fineractProperties.getDatabase().getDefaultMasterPassword());
        String encryption = Optional.ofNullable(fineractProperties.getTenant())
                .map(FineractProperties.FineractTenantProperties::getEncryption).orElse(DEFAULT_ENCRYPTION);
        return EncryptionUtil.decryptFromBase64(encryption, masterPassword, encryptedPassword);
    }

    public String getMasterPasswordHash() {
        String masterPassword = Optional.ofNullable(fineractProperties) //
                .map(FineractProperties::getTenant) //
                .map(FineractProperties.FineractTenantProperties::getMasterPassword) //
                .orElse(fineractProperties.getDatabase().getDefaultMasterPassword());
        return BCrypt.hashpw(masterPassword.getBytes(StandardCharsets.UTF_8), BCrypt.gensalt());
    }

    public boolean isMasterPasswordHashValid(String hashed) {
        String masterPassword = Optional.ofNullable(fineractProperties) //
                .map(FineractProperties::getTenant) //
                .map(FineractProperties.FineractTenantProperties::getMasterPassword) //
                .orElse(fineractProperties.getDatabase().getDefaultMasterPassword());
        return BCrypt.checkpw(masterPassword, hashed);
    }
}
