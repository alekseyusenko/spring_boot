package com.example.demo.repository;

import com.example.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleDao extends JpaRepository <Role, Long> {


}
