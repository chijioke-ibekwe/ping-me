package com.project.pingme.repository;

import com.project.pingme.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void testFindByFirstNameOrLastNameContainingIgnoreCase(){
        User userOne = User.builder()
                .firstName("Stan")
                .lastName("Gallagher")
                .username("stan.gallagher")
                .build();

        User userTwo = User.builder()
                .firstName("Mark")
                .lastName("Pitt")
                .username("mark.pitt")
                .build();

        Arrays.asList(userOne, userTwo).forEach(r -> testEntityManager.persistAndFlush(r));

        List<User> result = userRepository.findByFirstNameOrLastNameContainingIgnoreCase("gALLagHEr");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUsername()).isEqualTo("stan.gallagher");
    }
}
