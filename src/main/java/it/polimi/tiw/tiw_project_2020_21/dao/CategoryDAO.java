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
        String query = "SELECT * FROM level_0 ORDER BY last_modified ASC";
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
        String query = "SELECT * FROM level_" + level + " WHERE parent = '" + parent.getName() + "' ORDER BY last_modified ASC";
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

    public void createNewCategory(String name, int parentLevel, String parentName) throws SQLException
    {
        int level = parentLevel + 1;
        String query;
        if(level > 0)
        {
            if(!tableExistsSQL(connection,"level_"+level))
            {
                createNewTable(level);
            }
            query = "insert into level_"+ level +" values('"+ name +"', '" + parentName + "', now())";
        }
        else
        {
            query = "insert into level_"+ level +" values('"+ name +"', now())";
        }
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }

    public void updateFather(String name, int level, String newParentName) throws SQLException
    {
        String query;
        if (level == 0)
            return;
        if(tableExistsSQL(connection,"level_"+level))
        {
            query = "UPDATE level_" + level + " SET parent='" + newParentName + "' WHERE name='" + name + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        }
    }

    public void removeCategory(String name, int level) throws SQLException {
        if(tableExistsSQL(connection,"level_"+level))
        {
            String query = "DELETE FROM level_" + level + " WHERE name ='" + name + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        }
    }

    public void updateLevel(ArrayList<Category> categories, int newLevel, String parentName) throws SQLException
    {
        if (categories == null || categories.size() == 0)
            return;
        if(!tableExistsSQL(connection,"level_"+newLevel))
        {
            createNewTable(newLevel);
        }
        for (Category category: categories)
        {
            removeCategory(category.getName(), category.getLevel());
            createNewCategory(category.getName(), newLevel-1, parentName);
            //String query = "INSERT INTO level_" + newLevel + " (name, parent, last_modified)\n" +
            //       "          SELECT name, parent, last_modified\n" +
            //       "          FROM level_" + category.getLevel() + "\n" +
            //       "          WHERE name='" + category.getName() +"' ; \n" +
            //       "    DELETE FROM level_" + category.getLevel() + "\n" +
            //       "          WHERE name='" + category.getName() +" ' ;";
            updateLevel(category.getSubCategories(), newLevel + 1, category.getName());
        }
    }

    public void moveCategory(Category category, int newParentLevel, String newParentName) throws SQLException
    {
        removeCategory(category.getName(), category.getLevel());
        createNewCategory(category.getName(), newParentLevel, newParentName);
        updateLevel(category.getSubCategories(), newParentLevel + 2, category.getName());
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
            idString = idString.substring(1, idString.length());
        }
        return null;
    }
    private void createNewTable(int level) throws SQLException {
        String query = "CREATE TABLE `level_" + level + "` (\n" +
                        "  `name` VARCHAR(100) NOT NULL,\n" +
                        "  `parent` VARCHAR(100) NOT NULL,\n" +
                        "  `last_modified` DATETIME NOT NULL,\n" +
                        "  PRIMARY KEY (`name`),\n" +
                        "  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);";
        connection.prepareStatement(query).executeUpdate();
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