package it.polimi.tiw.tiw_project_2020_21.controllers;

import it.polimi.tiw.tiw_project_2020_21.util.Initializer;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/CreateCategory")
public class CreateCategory extends HttpServlet
{
    private static Connection connection;
    private static TemplateEngine templateEngine;

    @Override
    public void init() {
        connection = Initializer.connectionInit(getServletContext());
        templateEngine = Initializer.templateEngineInit(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
