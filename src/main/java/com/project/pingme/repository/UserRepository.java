package com.project.pingme.repository;

import com.project.pingme.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query(value = "select * from users u where upper(first_name) like upper(concat('%', :name, '%')) or " +
            "upper(last_name) like upper(concat('%', :name, '%'))", nativeQuery = true)
    List<User> findByFirstNameOrLastNameContainingIgnoreCase(@Param("name") String name);

    List<User> findByPhoneNumberContainingIgnoreCase(String phoneNumber);

    List<User> findByUsernameContainingIgnoreCase(String username);
}
