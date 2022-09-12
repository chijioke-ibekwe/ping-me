package com.project.pingme.repository;

import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Long> {

    List<UserContact> findByHostOrContact(User user_, User user);
}
