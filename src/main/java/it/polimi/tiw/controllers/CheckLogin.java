package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.util.Initializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "CheckLogin", value = "/CheckLogin")
public class CheckLogin extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public void init() {
        //connection = Initializer.connectionInit(getServletContext());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usrn = request.getParameter("username");
        String pwd = request.getParameter("password");
        Boolean fieldsAreValid = true;

        System.out.println("Checking user " + usrn + " with password " + pwd);

        if (usrn == null || usrn.isEmpty()) {
            response.getWriter().println("Missing username");
            fieldsAreValid = false;
        }
        if (pwd == null || pwd.isEmpty()) {
            response.getWriter().println("Missing password");
            fieldsAreValid = false;
        }

        if (!fieldsAreValid)
            return;

        // Tries to get connection from db
        try {
            connection = Initializer.connectionInit(getServletContext());
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

        UserDAO userDAO = new UserDAO(connection);

        User user = null;
        try {
            System.out.println("Trying to check credentials...");
            user = userDAO.checkCredentials(usrn, pwd);
            System.out.println("Credentials checked");
        } catch (SQLException e) {
            response.getWriter().println("Failure in database credential checking");
            return;
        }

        // Tries to close connection from db after executing the query
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignored) {}

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Incorrect username or password");
        } else {
            request.getSession().setAttribute("user", user);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Logged in");
        }
    }

    public void destroy() {}
}
