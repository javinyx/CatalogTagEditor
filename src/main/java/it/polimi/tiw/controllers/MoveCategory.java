package it.polimi.tiw.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.tiw.beans.Category;
import it.polimi.tiw.beans.Change;
import it.polimi.tiw.dao.CategoryDAO;
import it.polimi.tiw.util.Initializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
@WebServlet(value = "/MoveCategory")
public class MoveCategory extends HttpServlet
{
    private static Connection connection;

    @Override
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
                    return;
                }

                if(categoryDAO.isParent(newParent, category))
                {
                    session.setAttribute("movementError", "Cannot move category to himself or to children!");
                    response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");

                    revertChanges();
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

                categoryDAO.updateFatherDatabase(category.getDatabaseId(),  newParent.getDatabaseId());
                session.removeAttribute("movementError");
            }catch (SQLException e)
            {
                e.printStackTrace();
                session.setAttribute("movementError", "Cannot apply changes to the server!");
                try {
                    revertChanges();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }


        }
        //TODO
        // make queries
        //Tries to close connection from db after executing the query
        try {
            applyChanges();
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignored) {}
        response.sendRedirect(getServletContext().getContextPath() + "/");
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
    }
}