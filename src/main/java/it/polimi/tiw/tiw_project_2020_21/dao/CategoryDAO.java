package it.polimi.tiw.tiw_project_2020_21.dao;

import it.polimi.tiw.tiw_project_2020_21.beans.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDAO
{
    private final Connection connection;

    public CategoryDAO(Connection connection)
    {
        this.connection = connection;
    }
    public ArrayList<Category> findAllCategories() throws SQLException
    {
        ArrayList<Category> categories = new ArrayList<Category>();
        Category root = new Category(0, "root", 0);
        root.setSubCategories(findAllChildrenCategory(0, 0));
        categories.add(root);
        return categories;
    }

    public ArrayList<Category> findAllChildrenCategory(int parentID, int parentCount) throws SQLException
    {
        ArrayList<Category> categories;
        String query = "SELECT * FROM categories WHERE parent_id = ? ORDER BY last_modified ASC";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, parentID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                int count = 1;
                categories = new ArrayList<>();
                while (resultSet.next()) {
                    Category category = new Category(parentCount * 10 + count, resultSet.getString("name"), resultSet.getInt("id"));
                    category.setSubCategories(findAllChildrenCategory(category.getDatabaseId(), category.getId()));
                    categories.add(category);
                    count++;
                }
            }
        }
        return categories;
    }
    public void createNewCategory(String name, int parentDatabaseId) throws SQLException
    {
        String query;
        query = "insert into categories (name, last_modified, parent_id) values (?, now(), ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, parentDatabaseId);
        preparedStatement.executeUpdate();
    }

    public void updateFather(int databaseId, int parentDatabaseId) throws SQLException
    {
        String query;
            query = "UPDATE categories SET parent_id = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, parentDatabaseId);
            preparedStatement.setInt(2, databaseId);
            preparedStatement.executeUpdate();
    }

    public void moveCategory(int databaseId, int parentDatabaseId) throws SQLException
    {
        updateFather(databaseId, parentDatabaseId);
    }

    public Category findCategory(int id, ArrayList<Category> categories)
    {
        String idString = Integer.toString(id);
        while (idString.length() > 0)
        {
            int index = Integer.parseInt(String.valueOf(idString.charAt(0)));
            if(categories.get(index-1).getId() == id)
                return categories.get(index-1);
            categories = categories.get(index-1).getSubCategories();
            idString = idString.substring(1);
        }
        return null;
    }
}