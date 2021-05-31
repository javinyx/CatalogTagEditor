package it.polimi.tiw.tiw_project_2020_21.dao;

import it.polimi.tiw.tiw_project_2020_21.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection con;

    public UserDAO(Connection con) {
        this.con = con;
    }

    public User checkCredentials(String username, String password) throws SQLException {
        String query = "SELECT username FROM users  WHERE username = ? AND password = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, username);
            pstatement.setString(2, password);
            System.out.println("Statement prepared");
            try (ResultSet result = pstatement.executeQuery()) {
                System.out.println("Query executed");
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return null;
                else {
                    result.next();
                    User user = new User();
                    user.setUsername(result.getString("username"));
                    System.out.println("Created User object");
                    return user;
                }
            }
        }
    }
}
