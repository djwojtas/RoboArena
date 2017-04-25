package pl.edu.agh.kis.woznwojc.roboarena_server.db;

import pl.edu.agh.kis.woznwojc.roboarena_server.db.h2_implementation.H2DataBaseInitialization;
import pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces.DataBaseInitialization;

/**
 * Wrapper around {@link H2DataBaseInitialization} object to make methods static
 */
public class DataBaseSetup {
    /**
     * Executes all required DB statements
     */
    public static void executeStatements() {
        DataBaseInitialization initializer = new H2DataBaseInitialization();
        initializer.executeStatements();
    }
}
