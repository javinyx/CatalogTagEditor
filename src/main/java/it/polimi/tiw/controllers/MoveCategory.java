package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.Category;
import it.polimi.tiw.dao.CategoryDAO;
import it.polimi.tiw.util.Initializer;
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

    @Override
    public void init() {
        connection = Initializer.connectionInit(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        WebContext webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        ArrayList<Category> categories;
        int parentId = Integer.parseInt(request.getParameter("newParent"));
        int toMoveId = Integer.parseInt(request.getParameter("toMove"));
        Category newParent;
        Category category;
        try
        {
            CategoryDAO categoryDAO = new CategoryDAO(connection);
            categories = categoryDAO.findAllCategories();
            category = categoryDAO.findCategory(toMoveId, categories.get(0).getSubCategories());
            if (parentId == 0)
            {
                //move to root
                newParent = new Category(0, "root", 0);
            }
            else
            {
                newParent = categoryDAO.findCategory(parentId, categories.get(0).getSubCategories());
            }
            categoryDAO.moveCategory(category.getDatabaseId(),  newParent.getDatabaseId());
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