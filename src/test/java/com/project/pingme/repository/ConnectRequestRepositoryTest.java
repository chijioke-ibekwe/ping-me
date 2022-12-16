package com.project.pingme.repository;

import com.project.pingme.entity.ConnectRequest;
import com.project.pingme.entity.User;
import com.project.pingme.enums.RequestStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ConnectRequestRepositoryTest {

    @Autowired
    private ConnectRequestRepository connectRequestRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void testFindBySenderAndRequestStatus(){
        User userOne = User.builder().username("john.doe").build();
        User userTwo = User.builder().username("jane.doe").build();
        User userThree = User.builder().username("mark.pitt").build();
        User userFour = User.builder().username("ronald.rush").build();

        Arrays.asList(userOne, userTwo, userFour).forEach(r -> testEntityManager.persistAndFlush(r));
        User savedUserThree = testEntityManager.persistFlushFind(userThree);

        ConnectRequest connectRequestOne = ConnectRequest.builder()
                .sender(userOne)
                .recipient(userTwo)
                .requestStatus(RequestStatus.PENDING)
                .build();

        ConnectRequest connectRequestTwo = ConnectRequest.builder()
                .sender(userThree)
                .recipient(userFour)
                .requestStatus(RequestStatus.ACCEPTED)
                .build();

        Arrays.asList(connectRequestOne, connectRequestTwo).forEach(r -> testEntityManager.persistAndFlush(r));

        List<ConnectRequest> result = connectRequestRepository.findBySenderAndRequestStatus(savedUserThree.getId(),
                RequestStatus.ACCEPTED.name(), RequestStatus.REJECTED.name());

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(connectRequestTwo);
    }
}
