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
        ArrayList<Category> categories;
        String query = "SELECT * FROM level_0 ORDER BY last_modified DESC";
        try (ResultSet resultSet = connection.prepareStatement(query).executeQuery())
        {
            categories = new ArrayList<>();
            int count = 1;
            while (resultSet.next())
            {
                Category category = new Category(count, 0, resultSet.getString(1));
                category.setSubCategories(findAllSubCategory(category, 1));
                categories.add(category);
                count++;
            }
        }
        return categories;
    }
    public ArrayList<Category> findAllSubCategory(Category parent, int level) throws  SQLException
    {
        ArrayList<Category> categories;
        String query = "SELECT * FROM level_" + level + " WHERE parent = '" + parent.getName() + "' ORDER BY last_modified DESC";
        if(tableExistsSQL(connection,"level_"+level))
        {
            try (ResultSet resultSet = connection.prepareStatement(query).executeQuery())
            {
                int count = 1;

                categories = new ArrayList<>();
                while (resultSet.next())
                {
                    Category category = new Category(parent.getId()*10 + count, level, resultSet.getString(1));
                    category.setSubCategories(findAllSubCategory(category, level+1));
                    categories.add(category);
                    count++;
                }
            }
            return categories;
        }
        return null;
    }

    static boolean tableExistsSQL(Connection connection, String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) "
                + "FROM information_schema.tables "
                + "WHERE table_name = ?"
                + "LIMIT 1;");
        preparedStatement.setString(1, tableName);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) != 0;
    }
}