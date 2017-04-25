package pl.edu.agh.kis.woznwojc.roboarena_server.passwords;

import pl.edu.agh.kis.woznwojc.roboarena_server.passwords.bcrypt_implementation.BCryptHashes;
import pl.edu.agh.kis.woznwojc.roboarena_server.passwords.interfaces.Hashing;

/**
 * Wrapper around {@link Hashing} object to make methods static
 */
public class PasswordHandler {
    /**
     * {@link Hashing} object used to do DB operations
     */
    private static final Hashing encryptor = new BCryptHashes(12);

    /**
     * Hashes given <b>password</b>
     * @param password Plaintext password
     * @return Hashed password
     */
    public static String hashPassword(String password) {
        return encryptor.hashPassword(password);
    }

    /**
     * Compares already hashed password with plaintext password
     * @param plain Plaintext password to hash and compare
     * @param hash Hashed password to hash and compare with already hashed password
     * @return True if passwords are same otherwise returns false
     */
    public static boolean comparePassword(String plain, String hash) {
        return encryptor.comparePassword(plain, hash);
    }
}