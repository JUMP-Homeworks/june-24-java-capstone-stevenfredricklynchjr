package com.cognixia.jump.util;

import org.mindrot.jbcrypt.BCrypt;

/*
 * 
 * A password utility for security best practices
 * 
 * Used to hash passwords to prevent SQL injection, by implementing 
 * the BCrypt library
 * 
 */

public class PasswordUtil {

    // hash a password
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // check a password
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
	
}
