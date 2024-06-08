package com.fineract.mifos.mifos_core.useradministration.entity;

import com.fineract.mifos.mifos_core.infrastructure.core.domain.AbstractPersistableCustom;
import com.fineract.mifos.mifos_core.portifolio.client.entity.Client;
import jakarta.persistence.*;


@Entity
@Table(name = "m_selfservice_user_client_mapping")
public class AppUserClientMapping extends AbstractPersistableCustom {
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "appuser_id", nullable = false)
    private AppUser appUser;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public AppUserClientMapping() {

}
