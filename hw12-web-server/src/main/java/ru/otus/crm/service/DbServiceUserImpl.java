package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.User;

import java.util.List;
import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger log = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceUserImpl(TransactionManager transactionManager, DataTemplate<User> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.userDataTemplate = clientDataTemplate;
    }

    @Override
    public User saveUser(User user) {
        return transactionManager.doInTransaction(session -> {
            if (user.getId() == null) {
                userDataTemplate.insert(session, user);
                log.info("created user: {}", user);
                return user;
            }
            userDataTemplate.update(session, user);
            log.info("updated user: {}", user);
            return user;
        });
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            List<User> user = userDataTemplate.findByEntityField(session, "login", login);
            return user.stream().findFirst();
        });
    }


}
