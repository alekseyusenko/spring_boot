package com.example.demo.security;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        return user;
    }

    public List<User> allUsers() {
        return userDao.findAll();
    }

    public User findUserById(Long id) {
        Optional<User> user = userDao.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else throw new NoResultException("User with id " + id + " not found");
    }

    public boolean saveUser(User user) {
        User userFromDb = userDao.findByUsername(user.getUsername());
        if (userFromDb != null) {
            throw new IllegalStateException("User " + user.getUsername() + " already exists");
        }
        checkRoles(user);
        userDao.save(user);
        return true;
    }

    private void checkRoles(User user) {
        Collection<Role> roles = new HashSet<>();
        roles.add(new Role(1L, "ROLE_ADMIN"));
        roles.add(new Role(2L, "ROLE_USER"));

        if (user.isUserLabel() && user.isAdminLabel()) {
            user.setRoles(roles);
        } else if (user.isAdminLabel()) {
            user.setRoles((Collections.singleton(new Role(1L, "ROLE_ADMIN"))));
        } else if (user.isUserLabel()) {
            user.setRoles((Collections.singleton(new Role(2L, "ROLE_USER"))));
        } else {
            throw new IllegalStateException("Choose at least one role");
        }
    }

    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    public void editUser(User user) {
        User userFromDb = findUserById(user.getId());
        if (!userFromDb.getUsername().equals(user.getUsername())) {
            User testUser = userDao.findByUsername(user.getUsername());
            if (testUser != null) {
                throw new IllegalStateException("User " + user.getUsername() + " already exists");
            }
        }
        checkRoles(user);
        userDao.save(user);
    }
}



