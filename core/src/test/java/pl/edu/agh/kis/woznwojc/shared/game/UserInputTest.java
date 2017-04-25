package pl.edu.agh.kis.woznwojc.shared.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserInputTest {
    @Test
    public void getStringRepresentation() throws Exception {
        UserInput test = new UserInput();
        test.keyA = true;
        assertEquals("01000", test.getStringRepresentation());
    }

    @Test
    public void buildFromString() throws Exception {
        UserInput test = new UserInput();
        test.buildFromString("10101");
        assertTrue(test.keyW);
        assertFalse(test.keyA);
    }

}