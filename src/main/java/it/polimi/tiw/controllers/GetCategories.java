package it.polimi.tiw.controllers;

import com.google.gson.Gson;
import it.polimi.tiw.beans.Category;
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

@WebServlet(name = "GetCategories" ,value = "/GetCategories")
public class GetCategories extends HttpServlet {

    private static Connection connection;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        connection = Initializer.connectionInit(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<Category> categories = null;
        try {
            CategoryDAO categoryDAO = new CategoryDAO(connection);
            categories = categoryDAO.findAllCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(gson.toJson(categories));
    }
}
