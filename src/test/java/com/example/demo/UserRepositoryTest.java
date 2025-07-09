package com.example.demo;

//package com.example.RestApiSpring.repository;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;
    private List<User> manyUsers;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        User initialUser1 = new User("John Doe", "john.doe@example.com");
        User initialUser2 = new User("Vikash Patel", "vikash.patel@example.com");

        List<User> savedInitialUsers = userRepository.saveAll(Arrays.asList(initialUser1, initialUser2));
        this.user1 = savedInitialUsers.get(0);
        this.user2 = savedInitialUsers.get(1);

        manyUsers = IntStream.rangeClosed(1, 25)
                .mapToObj(i -> new User("User" + i, "user" + i + "@example.com"))
                .collect(Collectors.toList());
        userRepository.saveAll(manyUsers);
    }

    @Test
    @DisplayName("Find user by email")
    public void testFindByEmailFound() {
        Optional<User> found = userRepository.findByEmail(user1.getEmail());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(user1.getName());
        assertThat(found.get().getEmail()).isEqualTo(user1.getEmail());
        assertThat(found.get().getId()).isEqualTo(user1.getId());
    }
    @Test
    @DisplayName("Find user by email - not found")
    public void testFindByEmailNotFound() {
        Optional<User> found = userRepository.findByEmail("notfound@example.com");
        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("Save user and verify persistence")
    public void testSaveUser() {

        User user = new User("Charlie chaplin", "charlie@example.com");
        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Charlie chaplin");
    }

    @Test
    @DisplayName("Update user and verify changes")
    public void testUpdateUser() {
        String newName = "manoj reddy";
        String newEmail = "manojreddy@gmail.com";

        //this should be in setup not in this method
//        User user3 = new User("Manja", "Manja@example.com");
//
//        user3.setName(newName);
//        user3.setEmail(newEmail);
//
//        User updatedUser = userRepository.save(user3);

    }
}