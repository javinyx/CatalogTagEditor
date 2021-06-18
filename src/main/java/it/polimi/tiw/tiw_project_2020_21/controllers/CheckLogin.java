package it.polimi.tiw.tiw_project_2020_21.controllers;

import it.polimi.tiw.tiw_project_2020_21.beans.User;
import it.polimi.tiw.tiw_project_2020_21.dao.UserDAO;
import it.polimi.tiw.tiw_project_2020_21.util.Initializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "CheckLogin", value = "/CheckLogin")
public class CheckLogin extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public CheckLogin() {
        super();
    }

    public void init() {
        connection = Initializer.connectionInit(getServletContext());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String usrn = request.getParameter("username");
        String pwd = request.getParameter("pwd");
        String path = getServletContext().getContextPath() + "/login.html";
        if (usrn == null || usrn.isEmpty() || pwd == null || pwd.isEmpty())
        {
            session.setAttribute("loginError", "Missing parameter. Error " + HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("GoToLogin");
            return;
        }

        UserDAO userDAO = new UserDAO(connection);

        User user;
        try {
            System.out.println("Trying to check credentials...");
            user = userDAO.checkCredentials(usrn, pwd);
            System.out.println("Credentials checked");
        } catch (SQLException e)
        {
            //response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database credential checking");
            session.setAttribute("loginError", "Failure in database credential checking. Error " + HttpServletResponse.SC_BAD_GATEWAY);
            response.sendRedirect(path);
            return;
        }

        if (user == null)
        {
            session.setAttribute("loginError", "Incorrect username or password.");
        } else
        {
            request.getSession().setAttribute("user", user);
            String target = "/GoToHomePage";
            path = path + target;
            session.removeAttribute("loginError");
        }
        response.sendRedirect(path);
    }

    public void destroy() {}
}
