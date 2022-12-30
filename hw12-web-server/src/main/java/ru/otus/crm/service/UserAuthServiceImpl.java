package ru.otus.crm.service;


public class UserAuthServiceImpl implements UserAuthService {

    private final DBServiceUser dBServiceUser;

    public UserAuthServiceImpl(DBServiceUser dBServiceUser) {
        this.dBServiceUser = dBServiceUser;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dBServiceUser.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

}
