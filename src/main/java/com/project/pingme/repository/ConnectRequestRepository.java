package com.project.pingme.repository;

import com.project.pingme.entity.ConnectRequest;
import com.project.pingme.entity.User;
import com.project.pingme.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectRequestRepository extends JpaRepository<ConnectRequest, Long> {

    @Query(value = "select * from connect_requests where sender_id=:senderId and (request_status=:requestStatus " +
            "or request_status=:_requestStatus)", nativeQuery = true)
    List<ConnectRequest> findBySenderAndRequestStatus(@Param("senderId") Long senderId,
                                                      @Param("requestStatus") String requestStatus,
                                                      @Param("_requestStatus") String _requestStatus);

    List<ConnectRequest> findByRecipientAndRequestStatus(User user, RequestStatus requestStatus);

    Optional<ConnectRequest> findByRecipientAndId(User recipient, Long connectRequestId);

    boolean existsBySenderAndRecipientAndRequestStatus(User user, User _user, RequestStatus requestStatus);
}
