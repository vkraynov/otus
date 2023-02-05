package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.model.Client;

/**
 * ClientRepository.
 *
 * @author Vadim_Kraynov
 * @since 05.02.2023
 */
public interface ClientRepository extends CrudRepository<Client, Long> {
}
