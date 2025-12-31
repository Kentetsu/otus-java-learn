package ru.web.webServer.services;

import ru.web.webServer.dao.UserDao;

public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    public UserAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return userDao.findByName(login).stream()
                .findFirst()
                .map(user -> {
                    boolean isPasswordCorrect = user.getPassword().equals(password);
                    boolean isAdmin = Boolean.TRUE.equals(user.getIsAdmin());
                    System.out.println(user.getPassword() + " " + user.getIsAdmin());
                    return isPasswordCorrect && isAdmin;
                })
                .orElse(false);
    }
}
