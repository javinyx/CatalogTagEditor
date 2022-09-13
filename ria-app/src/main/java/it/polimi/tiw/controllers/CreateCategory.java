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
        //connection = Initializer.connectionInit(getServletContext());
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
            response.setStatus(400);
            response.getWriter().println("Parent value is not correct");
            return;
        }

        // Tries to get connection from db
        try {
            connection = Initializer.connectionInit(getServletContext());
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

        CategoryDAO categoryDAO = new CategoryDAO(connection);

        //check if name is not empty
        if (name.equals("") || name.length() > 50 || categoryParent == null) {
            response.getWriter().println("Category name must not be empty");
            response.setStatus(400);
            return;
        }

        try {
            //check if parent exists in database
            if(categoryDAO.findCategoryDatabaseId(parentDatabaseId, categoryDAO.findAllCategories()) == null)
            {
                response.setStatus(400);
                response.getWriter().println("Parent does not exists in database");
                return;
            }
            //check if already exists a category with this name
            if (categoryDAO.alreadyExist(name))
            {
                response.setStatus(400);
                response.getWriter().println("Category already exists");
                return;
            }
            categoryDAO.createNewCategory(name, parentDatabaseId);
        }
        catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().println("Internal server error");
            return;
        }
        response.setStatus(200);

        // Tries to close connection from db after executing the query
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignored) {}
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
