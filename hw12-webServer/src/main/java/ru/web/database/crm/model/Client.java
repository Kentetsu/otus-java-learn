package ru.web.database.crm.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"address", "phones", "password"})
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

    @Column(name = "password")
    private String password = "11111";

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Expose
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
        this.isAdmin = false;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
        this.isAdmin = false;
    }

    public Client(Long id, String name, String password, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        if (phones != null) {
            this.phones = phones;
            this.phones.forEach(phone -> phone.setClient(this));
        }
        this.isAdmin = false;
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
        clonedClient.setPassword(this.password);
        clonedClient.setIsAdmin(this.isAdmin);

        return clonedClient;
    }
}
