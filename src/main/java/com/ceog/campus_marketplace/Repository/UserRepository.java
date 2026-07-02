package com.ceog.campus_marketplace.Repository;

import com.ceog.campus_marketplace.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
//    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);

    // find by name AND college (both partial/case-insensitive)
    Page<User> findByUsernameContainingIgnoreCaseAndCollegeIgnoreCase(
            String username,
            String college,
            Pageable pageable);
    // Search users by username (for admin panel search box)
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    Optional<User> findByMobile(String mobile);

    // Find users by college
    Page<User> findByCollegeIgnoreCase(String college, Pageable pageable);

    // Find all users who have the ADMIN role
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = 'ADMIN'")
    List<User> findAllAdmins();

    // Count users by college (for admin stats)
    @Query("SELECT u.college, COUNT(u) FROM User u WHERE u.college IS NOT NULL GROUP BY u.college ORDER BY COUNT(u) DESC")
    List<Object[]> countUsersByCollege();
}
