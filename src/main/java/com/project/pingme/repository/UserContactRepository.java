package com.project.pingme.repository;

import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import com.project.pingme.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Long> {

    List<UserContact> findByHostOrContact(User user_, User user);

    @Query(value = "select * from user_contacts where (host_id=:userId and contact_id=:_userId) or (host_id=:_userId " +
            "and contact_id=:userId)", nativeQuery = true)
    UserContact findByHostAndContact(Long userId, Long _userId);
}
