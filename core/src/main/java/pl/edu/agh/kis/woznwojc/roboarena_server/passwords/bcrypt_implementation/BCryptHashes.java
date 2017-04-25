package pl.edu.agh.kis.woznwojc.roboarena_server.passwords.bcrypt_implementation;

import pl.edu.agh.kis.woznwojc.roboarena_server.passwords.interfaces.Hashing;

/**
 * Implementation of {@link Hashing} using BCrypt algorithm
 */
public class BCryptHashes implements Hashing{
    /**
     * Workload for generating salt
     */
    private final int workload;

    /**
     * Constructor to set workload
     * @param workload workload for generating salt
     */
    public BCryptHashes(int workload) {
        this.workload = workload;
    }
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(workload));
    }
    public boolean comparePassword(String plain, String hash) {
        return BCrypt.checkpw(plain, hash);
    }
}
