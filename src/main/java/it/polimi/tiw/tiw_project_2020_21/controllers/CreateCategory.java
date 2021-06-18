package it.polimi.tiw.tiw_project_2020_21.controllers;

import it.polimi.tiw.tiw_project_2020_21.dao.CategoryDAO;
import it.polimi.tiw.tiw_project_2020_21.util.Initializer;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "CreateCategory", value = "/CreateCategory")
public class CreateCategory extends HttpServlet
{
    private static Connection connection;

    @Override
    public void init()
    {
        connection = Initializer.connectionInit(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        HttpSession session = request.getSession();
        String name = request.getParameter("categoryName");
        String categoryParent = request.getParameter("categoryParent");
        int parentDatabaseId;
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
        if (name.equals("") || name.length() > 50 || categoryParent == null) {
            session.setAttribute("newCategoryError", "New category name must not be empty");
            response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
            return;
        }

        try {
            if(categoryDAO.findCategoryDatabaseId(parentDatabaseId, categoryDAO.findAllCategories()) == null)
            {
                session.setAttribute("newCategoryError", "Parent value is not correct");
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
