package ru.kata.spring.boot_security.demo.servises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

@Service
public class ServiceAdmin {
    private final UserDao userDao;

    @Autowired
    public ServiceAdmin(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return userDao.showAllUsers();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(int id) {
        userDao.delete(id);
    }

    public void update(int id, User user) {
        userDao.update(id, user);
    }

    public void save(User user) {
        userDao.save(user);
    }

}
