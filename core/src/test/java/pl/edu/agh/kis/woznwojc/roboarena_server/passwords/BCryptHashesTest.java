package pl.edu.agh.kis.woznwojc.roboarena_server.passwords;

import org.junit.Test;
import pl.edu.agh.kis.woznwojc.roboarena_server.passwords.bcrypt_implementation.BCryptHashes;
import pl.edu.agh.kis.woznwojc.roboarena_server.passwords.interfaces.Hashing;

import static org.junit.Assert.*;

public class BCryptHashesTest {
    Hashing encryptor = new BCryptHashes(12);
    String[] passwords = {
            "normalPASSWORD123",
            "",
            "specialCharsPass@\n\r%^\u1234\t\b\n\r\f\\\"\'",
            " ",
            "  ",
            "Verylongpasssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss"
    };

    @Test
    public void hashPassword() throws Exception {
        //salt test
        for(String pass : passwords) {
            String hashedPass = encryptor.hashPassword(pass);
            assertNotEquals(hashedPass, encryptor.hashPassword(pass));
        }

        //format test
        for(String pass : passwords) {
            String hashedPass = encryptor.hashPassword(pass);
            assertEquals(hashedPass.split("\\$").length, 4);
        }
    }

    @Test
    public void comparePassword() throws Exception {
        String[] hashedPasswords = new String[passwords.length];
        for(int i=0; i<passwords.length; ++i) {
            hashedPasswords[i] = encryptor.hashPassword(passwords[i]);
        }

        for(int i=0; i<passwords.length; ++i) {
            assertTrue(encryptor.comparePassword(passwords[i], hashedPasswords[i]));
        }

        for(int i=0; i<passwords.length; ++i) {
            assertFalse(encryptor.comparePassword("wrong", hashedPasswords[i]));
        }
    }
}