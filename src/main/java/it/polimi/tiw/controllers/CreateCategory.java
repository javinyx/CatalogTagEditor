package it.polimi.tiw.controllers;

import it.polimi.tiw.dao.CategoryDAO;
import it.polimi.tiw.util.Initializer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "CreateCategory", value = "/CreateCategory")
public class CreateCategory extends HttpServlet {
    private static Connection connection;

    @Override
    public void init() {
        connection = Initializer.connectionInit(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        HttpSession session = request.getSession();
        String name = request.getParameter("categoryName");
        String categoryParent = request.getParameter("categoryParent");
        int parentDatabaseId;
        //check if value is a number
        try
        {
            parentDatabaseId = Integer.parseInt(categoryParent);
        }
        catch (Exception e)
        {
            session.setAttribute("newCategoryError", "Parent value is not correct");
            response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
            return;
        }

        CategoryDAO categoryDAO = new CategoryDAO(connection);
        System.out.println();
        //check if name is not empty
        if (name.equals("") || name.length() > 50 || categoryParent == null) {
            session.setAttribute("newCategoryError", "New category name must not be empty");
            response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
            return;
        }

        try {
            //check if parent exists in database
            if(categoryDAO.findCategoryDatabaseId(parentDatabaseId, categoryDAO.findAllCategories()) == null)
            {
                session.setAttribute("newCategoryError", "Parent value is not correct");
                response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
                return;
            }
            //check if already exists a category with this name
            if (categoryDAO.alreadyExist(name))
            {
                session.setAttribute("newCategoryError", "Already exists a category with this name");
                response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
                return;
            }
            categoryDAO.createNewCategory(name, parentDatabaseId);
        }
        catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("newCategoryError", "Could not create new category\n" + e.getErrorCode());
            response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
            return;
        }
        session.removeAttribute("newCategoryError");
        response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
