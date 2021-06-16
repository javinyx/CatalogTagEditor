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

    public CheckLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init() {
        connection = Initializer.connectionInit(getServletContext());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usrn = request.getParameter("username");
        String pwd = request.getParameter("pwd");

        if (usrn == null || usrn.isEmpty() || pwd == null || pwd.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        UserDAO userDAO = new UserDAO(connection);

        User user = null;
        try {
            System.out.println("Trying to check credentials...");
            user = userDAO.checkCredentials(usrn, pwd);
            System.out.println("Credentials checked");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database credential checking");
            return;
        }

        // Tries to close connection from db after executing the query
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqle) {}

        String path = getServletContext().getContextPath();
        if (user == null) {
            path = getServletContext().getContextPath() + "/login.html";
        } else {
            request.getSession().setAttribute("user", user);
            String target = "/GoToHomePage";
            path = path + target;
        }
        response.sendRedirect(path);
    }

    public void destroy() {}
}
