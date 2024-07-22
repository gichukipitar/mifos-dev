package com.fineract.mifos.mifos_core.infrastructure.event.external.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "m_external_event_configuration")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalEventConfiguration {

    @Id
    @Column(name = "type", nullable = false)
    private String type;

    @Setter
    @Column(name = "enabled", nullable = false)
    private boolean enabled = false;

}
