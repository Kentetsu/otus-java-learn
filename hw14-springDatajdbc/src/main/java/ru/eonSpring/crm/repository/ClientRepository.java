package ru.eonSpring.crm.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.eonSpring.crm.model.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {}
