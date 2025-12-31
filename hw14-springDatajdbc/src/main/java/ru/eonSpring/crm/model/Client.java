package ru.eonSpring.crm.model;

import jakarta.annotation.Nonnull;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("client")
@Getter
@ToString
public class Client implements Persistable<Long> {

    @Id
    @Nonnull
    private Long id;

    private String name;

    @MappedCollection(idColumn = "id")
    private Address address;

    @MappedCollection(idColumn = "client_id", keyColumn = "client_id")
    private List<Phone> phones;

    @Transient
    private final boolean isNew;

    public Client() {
        this.isNew = true;
    }

    public Client(Long id, String name, Address address, List<Phone> phones, boolean isNew) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        this.isNew = isNew;
    }

    @PersistenceCreator
    private Client(Long id, String name, Address address, List<Phone> phones) {
        this(id, name, address, phones, false);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
