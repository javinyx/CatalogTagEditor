package it.polimi.tiw.controllers;

import it.polimi.tiw.dao.CategoryDAO;
import it.polimi.tiw.util.Initializer;
<<<<<<< HEAD

=======
>>>>>>> static-app/main
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "CreateCategory", value = "/CreateCategory")
<<<<<<< HEAD
public class CreateCategory extends HttpServlet {
    private static Connection connection;

    @Override
    public void init() {
        //connection = Initializer.connectionInit(getServletContext());
=======
public class CreateCategory extends HttpServlet
{
    private static Connection connection;

    @Override
    public void init()
    {
        connection = Initializer.connectionInit(getServletContext());
>>>>>>> static-app/main
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
<<<<<<< HEAD
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
=======
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
>>>>>>> static-app/main
            return;
        }

        try {
            //check if parent exists in database
            if(categoryDAO.findCategoryDatabaseId(parentDatabaseId, categoryDAO.findAllCategories()) == null)
            {
<<<<<<< HEAD
                response.setStatus(400);
                response.getWriter().println("Parent does not exists in database");
=======
                session.setAttribute("newCategoryError", "Parent value is not correct");
                response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
>>>>>>> static-app/main
                return;
            }
            //check if already exists a category with this name
            if (categoryDAO.alreadyExist(name))
            {
<<<<<<< HEAD
                response.setStatus(400);
                response.getWriter().println("Category already exists");
=======
                session.setAttribute("newCategoryError", "Already exists a category with this name");
                response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
>>>>>>> static-app/main
                return;
            }
            categoryDAO.createNewCategory(name, parentDatabaseId);
        }
        catch (SQLException e) {
            e.printStackTrace();
<<<<<<< HEAD
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
=======
            session.setAttribute("newCategoryError", "Could not create new category\n" + e.getErrorCode());
            response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
            return;
        }
        session.removeAttribute("newCategoryError");
        response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
>>>>>>> static-app/main
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
