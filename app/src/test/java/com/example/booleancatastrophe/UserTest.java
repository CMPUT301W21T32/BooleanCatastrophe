package com.example.booleancatastrophe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO add test cases for the owned experiments, subscriptions, and codes - need to see what functionality should be added and what is covered by the UserManager

public class UserTest {

    private User mockUser(int id) {
        User user = new User(id);
        user.setEmail("mock@user.ca");
        user.setUsername("Mr. Mock User");
        return user;
    }

    @Test
    void testUserID() {
        User user = mockUser(0);
        assertEquals(0, user.getDeviceID());

        User user2 = mockUser(420);
        assertEquals(420, user2.getDeviceID());
    }

    @Test
    void testUserEmail() {
        User user = mockUser(1);
        assertEquals("mock@user.ca", user.getEmail());

        user.setEmail("test@changed.com");
        assertEquals("test@changed.com", user.getEmail());

        assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
        });
    }

    @Test
    void testUserUsername() {
        User user = mockUser(2);
        assertEquals("Mr. Mock User", user.getUsername());

        user.setUsername("Bob");
        assertEquals("Bob", user.getUsername());

        assertThrows(IllegalArgumentException.class, () -> {
            user.setUsername("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
        });
    }

    @Test
    void testAddSubscriptions() {
        User user = mockUser(3);
    }
}
