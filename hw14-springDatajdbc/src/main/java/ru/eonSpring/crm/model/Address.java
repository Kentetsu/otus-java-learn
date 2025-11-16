package ru.eonSpring.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Address {
    @Id
    private Long id;

    private String street;
}
