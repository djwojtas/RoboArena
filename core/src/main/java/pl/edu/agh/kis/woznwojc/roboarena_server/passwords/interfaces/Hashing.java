package pl.edu.agh.kis.woznwojc.roboarena_server.passwords.interfaces;

/**
 * Interface that can hash plain passwords and compare hashes with plain passwords
 */
public interface Hashing {
    /**
     * Hashes given <b>password</b>
     * @param password Plaintext password
     * @return Hashed password
     */
    String hashPassword(String password);

    /**
     * Compares already hashed password with plaintext password
     * @param plain Plaintext password to hash and compare
     * @param hash Hashed password to hash and compare with already hashed password
     * @return True if passwords are same otherwise returns false
     */
    boolean comparePassword(String plain, String hash);
}