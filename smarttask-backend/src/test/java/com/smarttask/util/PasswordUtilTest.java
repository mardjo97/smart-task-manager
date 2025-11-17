package com.smarttask.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    public void testPasswordHashing() {
        String password = "testPassword123";
        String hashedPassword = PasswordUtil.hashPassword(password);
        
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }

    @Test
    public void testPasswordVerification() {
        String password = "testPassword123";
        String hashedPassword = PasswordUtil.hashPassword(password);
        
        assertTrue(PasswordUtil.verifyPassword(password, hashedPassword));
        assertFalse(PasswordUtil.verifyPassword("wrongPassword", hashedPassword));
    }
}
