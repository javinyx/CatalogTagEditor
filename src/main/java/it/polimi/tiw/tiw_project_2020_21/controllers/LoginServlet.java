package it.polimi.tiw.tiw_project_2020_21.controllers;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet
{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username != null) {
            request.getSession().setAttribute("user", "asmin");
            response.sendRedirect("index");
        }
        else {
            request.setAttribute("error", "Unknown user, please try again");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
    @Override
    public void destroy() {
        super.destroy();
    }
}
