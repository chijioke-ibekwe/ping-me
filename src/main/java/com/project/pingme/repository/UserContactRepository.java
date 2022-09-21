package com.project.pingme.repository;

import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import com.project.pingme.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Long> {

    List<UserContact> findByHostOrContactAndRequestStatus(User user_, User user, RequestStatus requestStatus);
}
