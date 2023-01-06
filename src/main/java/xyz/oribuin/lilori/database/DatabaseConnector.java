package xyz.oribuin.lilori.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnector {

    /**
     * Executes a callback with a Connection passed and automatically closes it when finished.
     *
     * @param callback The callback to execute once the connection is retrieved.
     */
    void connect(ConnectionCallback callback);
    /**
     * Closes all connections to the database.
     */
    void closeConnection();

    /**
     * @return the lock to notify when all connections have been finalized
     */
    Object getLock();

    /**
     * @return true if all connections have finished, otherwise false
     */
    boolean isFinished();

    /**
     * Cleans up the database data, if applicable
     */
    void cleanup();

    /**
     * Wraps a connection in a callback which will automagically handle catching sql errors
     */
    interface ConnectionCallback {
        void accept(Connection connection) throws SQLException;
    }

}
