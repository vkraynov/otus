package ru.otus.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Address.
 *
 * @author Vadim_Kraynov
 * @since 22.12.2022
 */
@Table(name = "address")
@Getter
public class Address {
    @Id
    private final Long clientId;
    private final String street;

    public Address(String street) {
        this(null, street);
    }

    @PersistenceCreator
    public Address(Long clientId, String street) {
        this.clientId = clientId;
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "clientId=" + clientId +
                ", street='" + street + '\'' +
                '}';
    }
}
