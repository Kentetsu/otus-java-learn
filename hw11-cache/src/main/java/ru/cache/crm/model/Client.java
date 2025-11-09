package ru.cache.crm.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"address", "phones"})
@Table(name = "client")
@Entity
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        if (phones != null) {
            this.phones = phones;
            this.phones.forEach(phone -> phone.setClient(this));
        }
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        Address cloneAddress = null;
        if (this.address != null) {
            cloneAddress = new Address(this.address.getId(), this.address.getStreet());
        }

        List<Phone> clonePhones = null;
        if (this.phones != null) {
            clonePhones = this.phones.stream()
                    .map(phone -> new Phone(phone.getId(), phone.getNumber()))
                    .toList();
        }

        Client clonedClient = new Client(this.id, this.name, cloneAddress, clonePhones);

        return clonedClient;
    }
}
