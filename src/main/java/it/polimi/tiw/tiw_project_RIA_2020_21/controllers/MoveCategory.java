package it.polimi.tiw.tiw_project_RIA_2020_21.controllers;

import it.polimi.tiw.tiw_project_RIA_2020_21.beans.Category;
import it.polimi.tiw.tiw_project_RIA_2020_21.dao.CategoryDAO;
import it.polimi.tiw.tiw_project_RIA_2020_21.util.Initializer;
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

@WebServlet(value = "/MoveCategory")
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
        WebContext webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        ArrayList<Category> categories = null;
        int parentId = Integer.parseInt(request.getParameter("newParent"));
        int toMoveId = Integer.parseInt(request.getParameter("toMove"));
        Category newParent;
        Category category = null;
        CategoryDAO categoryDAO = new CategoryDAO(connection);
        try
        {
            if (parentId == 0)
            {
                //move to root
                newParent = new Category(0, -1, "root");
            }
            else
            {
                categories = categoryDAO.findAllCategories();
                category = categoryDAO.findCategory(toMoveId, categories);
                newParent = categoryDAO.findCategory(parentId, categories);
            }
            if (newParent == null || category == null)
            {
                //we have a problem
            }
            if(newParent.getLevel() + 1 == category.getLevel())
            {
                //only need to update the first father because the level is the same -> children are in the same levels
                categoryDAO.updateFather(category.getName(), category.getLevel(), newParent.getName());
            }
            else
            {
                categoryDAO.moveCategory(category,  newParent.getLevel(), newParent.getName());
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
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