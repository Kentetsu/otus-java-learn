package ru.eonSpring.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("phones")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
    @Id
    private Long id;

    private String number;

    private Long clientId;
}
