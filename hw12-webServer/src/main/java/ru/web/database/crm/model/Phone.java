package ru.web.database.crm.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "phones")
@NoArgsConstructor
@Entity
public class Phone {
    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    @Expose(serialize = false)
    private Client client;
}
