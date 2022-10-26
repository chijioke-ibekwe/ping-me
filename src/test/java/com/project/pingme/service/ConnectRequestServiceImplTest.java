package com.project.pingme.service;

import com.project.pingme.dto.ConnectRequestDTO;
import com.project.pingme.entity.ConnectRequest;
import com.project.pingme.entity.User;
import com.project.pingme.enums.RequestStatus;
import com.project.pingme.repository.ConnectRequestRepository;
import com.project.pingme.service.impl.ConnectRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ConnectRequestServiceImpl.class)
class ConnectRequestServiceImplTest {

    @Autowired
    private ConnectRequestService connectRequestService;

    @MockBean
    private ConnectRequestRepository connectRequestRepository;

    @MockBean
    private UserContactService userContactService;

    @MockBean
    private UserService userService;

    private List<ConnectRequest> connectRequests = new ArrayList<>();

    @BeforeEach
    void setUp(){

        ConnectRequest connectRequest = ConnectRequest.builder()
                .id(1L)
                .recipient(User.builder()
                        .id(2L)
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .sender(User.builder()
                        .id(3L)
                        .firstName("Jane")
                        .lastName("Doe")
                        .build())
                .requestStatus(RequestStatus.PENDING)
                .build();

        connectRequests.add(connectRequest);
    }

    @Test
    void testGetReceivedConnectRequests(){

        when(connectRequestRepository.findByRecipientAndRequestStatus(any(User.class), any()))
                .thenReturn(connectRequests);

        List<ConnectRequestDTO> result = connectRequestService.getReceivedConnectRequests(new User());

        assertThat(result.get(0).getRequestId()).isEqualTo(1L);
        assertThat(result.get(0).getRecipientName()).isEqualTo("John Doe");
        assertThat(result.get(0).getSenderName()).isEqualTo("Jane Doe");
        assertThat(result.get(0).getRecipientId()).isEqualTo(2L);
    }
}
