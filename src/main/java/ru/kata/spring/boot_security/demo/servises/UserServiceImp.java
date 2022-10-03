package ru.kata.spring.boot_security.demo.servises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dao.UserDaoImp;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsImp;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService, UserDetailsService {
//    private final UsersRepository userRepository;
//    private final RoleRepository roleRepository;

    private final RoleDao roleDao;
    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(//UsersRepository userRepository,
                          //RoleRepository roleRepository,
                          UserDaoImp userDao,
                          PasswordEncoder passwordEncoder,
                          RoleDao roleDao) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Optional <User> user = userRepository.findUsersByUsername(username);
        User user = userDao.getUserByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("User not found!");
        return new UserDetailsImp(user);
    }

    @Override
    public List<User> showAllUsers() {
        return userDao.showAllUsers();
    }

    @Override
    public User showUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public void deleteUser(int id) {
        userDao.delete(id);
    }

    @Override
    public void updateUser(int id, User user) {
       // User userDB  = userRepository.findById(id).get();
        userDao.update(id, user);
    }

    @Transactional
    public boolean registerDefaultUser(User user) {
        //Optional<User> userDB  = userRepository.findUsersByUsername(user.getUsername());

//        if (!userDB.isEmpty()) {
//            return false;
//        }

        if (userDao.getUserByUsername(user.getUsername()) != null) {
            return false;
        }

        Set<Role> roles = new HashSet<>();
        //Role roleUser = roleRepository.findRoleByRoleName("ROLE_USER");
        Role roleUser = roleDao.findRoleByName("ROLE_USER");
        user.setRoles(Collections.singleton(roleUser));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //userRepository.save(user);
        userDao.save(user);
        return true;
    }
}
