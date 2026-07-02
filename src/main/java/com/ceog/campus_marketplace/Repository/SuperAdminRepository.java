package com.ceog.campus_marketplace.Repository;

import com.ceog.campus_marketplace.Model.SuperAdmin;
import com.ceog.campus_marketplace.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin,Long> {
    Optional<SuperAdmin> findById(Long id);
}
