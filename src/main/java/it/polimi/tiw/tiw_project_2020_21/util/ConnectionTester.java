package it.polimi.tiw.tiw_project_2020_21.util;

import it.polimi.tiw.tiw_project_2020_21.dao.UserDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ConnectionTester")
public class ConnectionTester extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        final String DB_URL = "jdbc:mysql://localhost:3306/tiw_2021";
        final String USER = "test";
        final String PASS = "test";
        String result = "Connection worked";
        try {

            Connection connection = Initializer.connectionInit(getServletContext());
            UserDAO userDAO = new UserDAO(connection);
            userDAO.checkCredentials("test", "test");
            /*

            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(DB_URL, USER, PASS);

             */
        } catch (Exception e) {
            result = "Connection failed";
            result += "\n";
            result += e.getMessage();
            e.printStackTrace();
        }
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println(result);
        out.close();
    }
}

