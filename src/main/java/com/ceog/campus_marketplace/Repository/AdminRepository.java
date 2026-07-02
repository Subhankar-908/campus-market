package com.ceog.campus_marketplace.Repository;

import com.ceog.campus_marketplace.Model.Admin;
import com.ceog.campus_marketplace.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    Optional<Admin> findByUsers(User users);

    void deleteByUsers(User users);
}
