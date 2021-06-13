package it.polimi.tiw.tiw_project_RIA_2020_21.controllers;

import it.polimi.tiw.tiw_project_RIA_2020_21.dao.CategoryDAO;
import it.polimi.tiw.tiw_project_RIA_2020_21.util.Initializer;

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
        int parentLevel = Integer.parseInt(categoryParent.split(" ", 2)[0]);
        String parentName = categoryParent.split(" ", 2)[1];
        CategoryDAO categoryDAO = new CategoryDAO(connection);
        System.out.println();
        if(name.equals("")) {
            session.setAttribute("newCategoryError", "Fields must not be empty!");
            response.sendRedirect(getServletContext().getContextPath() + "/GoToHomepage");
            return;
        }
        try {
            categoryDAO.createNewCategory(name, parentLevel, parentName);
        }
        catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to create a new category!");
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
