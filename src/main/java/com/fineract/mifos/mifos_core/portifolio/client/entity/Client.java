package com.fineract.mifos.mifos_core.portifolio.client.entity;

import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Table(name = "m_client", uniqueConstraints = { @UniqueConstraint(columnNames = { "account_no" }, name = "account_no_UNIQUE"), //
        @UniqueConstraint(columnNames = { "mobile_no" }, name = "mobile_no_UNIQUE") })
public class Client {
}
