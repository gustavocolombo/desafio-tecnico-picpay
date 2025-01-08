package com.picpaysimplificado.repositories;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.user.CreateUserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("it should be able to return user successfully from database")
    void findByDocumentWithSuccess() {
        String document = "99999999901";
        CreateUserDTO userTest = new CreateUserDTO(
                "John",
                "Doe",
                "johndoe@gmail.com",
                "password",
                document,
                UserType.COMMON,
                new BigDecimal(10)
        );

        this.createUser(userTest);

        Optional<User> result = this.userRepository.findByDocument(document);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("it should not be able return a user successfully from database")
    void findByDocumentWithFailure() {
        String document = "99999999901";

        Optional<User> notFoundedUser = this.userRepository.findByDocument(document);

        assertThat(notFoundedUser.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("it should be able return a user by email from database")
    void findByEmailWithSuccess () {
        CreateUserDTO userTest = new CreateUserDTO(
                "John",
                "Doe",
                "johndoe@gmail.com",
                "password",
                "99999999901",
                UserType.COMMON,
                new BigDecimal(10)
        );

        this.createUser(userTest);

        Optional<User> userFounded = this.userRepository.findByEmail(userTest.email());

        assertThat(userFounded.isPresent()).isTrue();
    }

    @Test
    @DisplayName("it should not be able a return user by email from database")
    void findByEmailWithError() {
        String email = "johndoe@gmail.com";

        Optional<User> notFoundedUser = this.userRepository.findByEmail(email);

        assertThat(notFoundedUser.isEmpty()).isTrue();
    }


    private User createUser(CreateUserDTO data) {
        User newUser = new User(data);
        entityManager.persist(newUser);

        return newUser;
    }
}