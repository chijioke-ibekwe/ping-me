package com.project.pingme.repository;

import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserContactRepositoryTest {

    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void testFindByHostAndContact(){
        User userOne = testEntityManager.persistFlushFind(User.builder().username("john.doe").build());
        User userTwo = testEntityManager.persistFlushFind(User.builder().username("jane.doe").build());
        User userThree = testEntityManager.persistFlushFind(User.builder().username("mark.pitt").build());

        UserContact userContactOne = UserContact.builder()
                .host(userOne)
                .contact(userThree)
                .build();

        UserContact userContactTwo = UserContact.builder()
                .host(userOne)
                .contact(userTwo)
                .build();

        UserContact userContactThree = UserContact.builder()
                .host(userTwo)
                .contact(userThree)
                .build();

        Arrays.asList(userContactOne, userContactTwo, userContactThree).forEach(r -> testEntityManager.persistAndFlush(r));

        UserContact result = userContactRepository.findByHostAndContact(userContactThree.getId(),
                userContactTwo.getId());

        assertThat(result.getHost().getUsername()).isEqualTo("jane.doe");
        assertThat(result.getContact().getUsername()).isEqualTo("mark.pitt");
    }
}
