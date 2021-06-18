package it.polimi.tiw.tiw_project_2020_21.controllers;

import it.polimi.tiw.tiw_project_2020_21.beans.Category;
import it.polimi.tiw.tiw_project_2020_21.dao.CategoryDAO;
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
        HttpSession session = request.getSession();
        ArrayList<Category> categories;
        int parentId = 0;
        int toMoveId = 0;
        try
        {
            parentId = Integer.parseInt(request.getParameter("newParent"));
            toMoveId = Integer.parseInt(request.getParameter("toMove"));
        }
        catch (NumberFormatException e)
        {
            session.setAttribute("movementError", "Cannot find category or new parent category!");
            response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
            return;
        }
        Category newParent;
        Category category;
        try
        {
            CategoryDAO categoryDAO = new CategoryDAO(connection);
            categories = categoryDAO.findAllCategories();
            category = categoryDAO.findCategoryDatabaseId(toMoveId, categories.get(0).getSubCategories());
            if (parentId == 0)
            {
                //move to root
                newParent = categories.get(0);
            }
            else
            {
                newParent = categoryDAO.findCategoryDatabaseId(parentId, categories.get(0).getSubCategories());
            }
            if (category == null || newParent == null)
            {
                session.setAttribute("movementError", "Cannot find category or new parent category!");
                response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
                return;
            }
            if(categoryDAO.isParent(newParent, category))
            {
                session.setAttribute("movementError", "Cannot move category to himself or to children!");
                response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
                return;
            }
            categoryDAO.moveCategory(category.getDatabaseId(),  newParent.getDatabaseId());
            session.removeAttribute("movementError");
        }catch (SQLException e)
        {
            e.printStackTrace();
            session.setAttribute("movementError", "Cannot apply changes to the server!");
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