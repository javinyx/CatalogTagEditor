package it.polimi.tiw.tiw_project_2020_21.controllers;

import it.polimi.tiw.tiw_project_2020_21.beans.Category;
import it.polimi.tiw.tiw_project_2020_21.dao.CategoryDAO;
import it.polimi.tiw.tiw_project_2020_21.util.Initializer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "GoToMoveCategory" ,value = "/GoToMoveCategory")
public class MoveCategory extends HttpServlet
{
    private static Connection connection;
    private static TemplateEngine templateEngine;

    @Override
    public void init() {
        connection = Initializer.connectionInit(getServletContext());
        templateEngine = Initializer.templateEngineInit(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.sendRedirect(getServletContext().getContextPath() + "/");
    }

    @Override
    public void destroy() {
        try {
            if(connection!=null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}