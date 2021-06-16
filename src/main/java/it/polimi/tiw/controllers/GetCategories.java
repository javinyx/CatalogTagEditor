package it.polimi.tiw.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.beans.Category;
import it.polimi.tiw.dao.CategoryDAO;
import it.polimi.tiw.util.ConnectionHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/GetCategories")
public class GetCategories extends HttpServlet {
    private static Connection connection = null;

    @Override
    public void init() throws ServletException {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categoryList = new ArrayList<Category>();
        CategoryDAO categoryDAO = new CategoryDAO(connection);

        try {
            categoryList = categoryDAO.findAllCategories();
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Unable to fetch categories.");
            return;
        }

        Gson gson = new Gson();
        String jsonOut = gson.toJson(categoryList);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonOut);
    }

    @Override
    public void destroy() {
        try {
            ConnectionHandler.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}