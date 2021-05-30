package it.polimi.tiw.tiw_project_2020_21.util;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHandler {

    private ConnectionHandler() {}

    public static Connection getConnection(ServletContext context) throws IllegalAccessException {
        Connection connection = null;
        try {
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            throw new IllegalAccessException("Could not get a database connection");
        }
        return connection;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if(connection!=null) {
            connection.close();
        }
    }
}