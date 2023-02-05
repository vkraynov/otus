package ru.otus.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "client")
@Getter
public class Client {
    @Id
    private final Long id;
    private final String name;
    @MappedCollection(idColumn = "client_id")
    private final Address address;

    public Client(String name, Address address) {
        this(null, name, address);
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
