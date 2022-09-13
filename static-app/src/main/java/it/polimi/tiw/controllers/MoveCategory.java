package it.polimi.tiw.controllers;

<<<<<<< HEAD
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.tiw.beans.Category;
import it.polimi.tiw.beans.Change;
=======
import it.polimi.tiw.beans.Category;
>>>>>>> static-app/main
import it.polimi.tiw.dao.CategoryDAO;
import it.polimi.tiw.util.Initializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
<<<<<<< HEAD
=======
import javax.servlet.http.HttpSession;
>>>>>>> static-app/main
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
<<<<<<< HEAD
import javax.servlet.http.HttpSession;
@WebServlet(name = "MoveCategory", value = "/MoveCategory")
=======

@WebServlet(value = "/MoveCategory")
>>>>>>> static-app/main
public class MoveCategory extends HttpServlet
{
    private static Connection connection;

    @Override
<<<<<<< HEAD
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Gson gson = new Gson();
        ArrayList<Change> changes = gson.fromJson(request.getParameter("changes"), new TypeToken<ArrayList<Change>>(){}.getType());

        CategoryDAO categoryDAO = null;
        ArrayList<Category> categories = null;
        // Tries to get connection from db
        try {
            connection = Initializer.connectionInit(getServletContext());
            connection.setAutoCommit(false);
            //get the updated category tree
            categoryDAO = new CategoryDAO(connection);
            categories = categoryDAO.findAllCategories();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

        if (categoryDAO == null)
            return;

        for (Change change: changes)
        {
            int parentId = change.getParentDatabaseId();
            int toMoveId = change.getDatabaseId();

            try
            {
                Category newParent;
                Category category;
                category = categoryDAO.findCategoryDatabaseId(toMoveId, categories.get(0).getSubCategories());

                if (parentId == 0)
                    newParent = categories.get(0);
                else
                    newParent = categoryDAO.findCategoryDatabaseId(parentId, categories.get(0).getSubCategories());

                if (category == null || newParent == null) {
                    session.setAttribute("movementError", "Cannot find category or new parent category!");
                    response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");

                    revertChanges();
                    closeConnection();
                    return;
                }

                if(categoryDAO.isParent(newParent, category))
                {
                    session.setAttribute("movementError", "Cannot move category to himself or to children!");
                    response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");

                    revertChanges();
                    closeConnection();
                    return;
                }
                Category oldParent;
                if(category.getId()/10 == 0)
                {
                    oldParent = categories.get(0);
                }
                else
                    oldParent = categoryDAO.findCategory(category.getId()/10, categories.get(0).getSubCategories());
                categoryDAO.moveCategory(category, oldParent, newParent);

                categoryDAO.updateFather(category.getDatabaseId(),  newParent.getDatabaseId());
                session.removeAttribute("movementError");
            }catch (SQLException e)
            {
                e.printStackTrace();
                session.setAttribute("movementError", "Cannot apply changes to the server!");
                try {
                    revertChanges();
                    closeConnection();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        closeConnection();
        //Tries to close connection from db after executing the query

        response.sendRedirect(getServletContext().getContextPath() + "/");
    }

    private void closeConnection()
    {
        try {
            applyChanges();
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignored) {}
    }

    private void revertChanges() throws SQLException
    {
        connection.rollback();
        connection.setAutoCommit(true);
    }
    private void applyChanges() throws SQLException
    {
        connection.commit();
        connection.setAutoCommit(true);
=======
    public void init() {
        connection = Initializer.connectionInit(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        ArrayList<Category> categories;
        int parentId;
        int toMoveId;
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
            categoryDAO.updateFather(category.getDatabaseId(),  newParent.getDatabaseId());
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
>>>>>>> static-app/main
    }
}