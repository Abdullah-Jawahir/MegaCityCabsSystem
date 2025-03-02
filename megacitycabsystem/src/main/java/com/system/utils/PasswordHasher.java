package com.system.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordHasher {
    private static final Logger logger = Logger.getLogger(PasswordHasher.class.getName());
    private static final int SALT_LENGTH = 16; // 128 bits

    /**
     * Hashes a password using SHA-256 algorithm with a random salt.
     *
     * @param password The password to hash.
     * @return Hashed password with salt (format: salt:hash).
     */
    public static String hash(String password) {
        try {
            // Generate a random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Create hash with salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));  // Specify UTF-8 encoding

            // Convert to Base64 for storage
            String saltString = Base64.getEncoder().encodeToString(salt);
            String hashString = Base64.getEncoder().encodeToString(hashedPassword);

            // Format as salt:hash
            return saltString + ":" + hashString;
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "Error hashing password", e);
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verifies a password against a stored hash.
     *
     * @param password   The password to verify.
     * @param storedHash The stored hash to check against (format: salt:hash) or a plaintext password.
     * @return true if password matches, false otherwise.
     */
    public static boolean verify(String password, String storedHash) {
        if (storedHash == null || storedHash.isEmpty()) {
            return false; // Or throw an IllegalArgumentException if null/empty is not allowed
        }

        if (storedHash.contains(":")) {
            return verifyHashedPassword(password, storedHash);
        } else {
            // Assume it's a plaintext password for legacy support.  Use UTF-8 encoding
            return password.equals(storedHash);
        }
    }

    /**
     * Verifies a password against a stored hash (format: salt:hash).
     *
     * @param password   The password to verify.
     * @param storedHash The stored hash to check against (format: salt:hash).
     * @return true if password matches, false otherwise.
     */
    private static boolean verifyHashedPassword(String password, String storedHash) {
        try {
            // Split the stored hash to get salt and hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false; // Invalid stored hash format
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);

            // Compute hash of provided password with same salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] computedHash = md.digest(password.getBytes(StandardCharsets.UTF_8)); // Specify UTF-8 encoding

            // Compare the hashes (constant time comparison)
            if (hash.length != computedHash.length) {
                return false; // Hashes have different lengths, cannot be equal
            }

            int diff = 0;
            for (int i = 0; i < hash.length; i++) {
                diff |= hash[i] ^ computedHash[i];
            }
            return diff == 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error verifying password", e);
            return false;
        }
    }
}