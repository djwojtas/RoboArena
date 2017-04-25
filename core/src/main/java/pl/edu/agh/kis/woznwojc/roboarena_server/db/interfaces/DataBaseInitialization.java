package pl.edu.agh.kis.woznwojc.roboarena_server.db.interfaces;

/**
 * Creates all tables necessary for server functionality
 */
public interface DataBaseInitialization {
    /**
     * Executes all required DB statements
     */
    void executeStatements();
}
