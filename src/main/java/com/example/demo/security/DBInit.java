package com.example.demo.security;


import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleDao;
import com.example.demo.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Component
public class DBInit {

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private RoleDao roleDao;

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @PostConstruct
    @Modifying
    @Transactional
    void postConstruct() throws InterruptedException {
        List<Role> roles = null;
        List<User> users = null;
        roles = roleDao.findAll();
        if (roles.isEmpty()) {
            roleDao.save(new Role(1L, "ROLE_ADMIN"));
            roleDao.save(new Role(2L, "ROLE_USER"));
        }
            users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("No users found. Creating default admin user.");
            Thread.sleep(3000);
            System.out.println("Credentials: username = admin, password = admin");
            Thread.sleep(3000);
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setAdminLabel(true);
            admin.setRoles(Collections.singleton(new Role(1L, "ROLE_ADMIN")));
            userDao.save(admin);
        }
        }
    }


