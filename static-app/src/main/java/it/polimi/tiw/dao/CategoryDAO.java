package it.polimi.tiw.dao;

import it.polimi.tiw.beans.Category;
<<<<<<< HEAD
import it.polimi.tiw.beans.Change;
=======
>>>>>>> static-app/main

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
<<<<<<< HEAD
        ArrayList<Category> categories = new ArrayList<Category>();
=======
        ArrayList<Category> categories = new ArrayList<>();
>>>>>>> static-app/main
        Category root = new Category(0, "root", 0);
        root.setSubCategories(findAllChildrenCategory(0, 0));
        categories.add(root);
        return categories;
    }

    public ArrayList<Category> findAllChildrenCategory(int parentID, int parentCount) throws SQLException
    {
        ArrayList<Category> categories;
<<<<<<< HEAD
        String query = "SELECT * FROM categories WHERE parent_id = ? ORDER BY last_modified ASC";
=======
        String query = "SELECT * FROM categories WHERE parent_id = ? ORDER BY last_modified";
>>>>>>> static-app/main
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
<<<<<<< HEAD

=======
>>>>>>> static-app/main
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
        query = "UPDATE categories SET parent_id = ?, last_modified = NOW()  WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, parentDatabaseId);
        preparedStatement.setInt(2, databaseId);
        preparedStatement.executeUpdate();
    }

<<<<<<< HEAD
    public void moveCategory(Category category, Category oldParent, Category newParent)
    {
        //updateFather(databaseId, parentDatabaseId)
        for(int i = 0; i < oldParent.getSubCategories().size(); i++)
        {
            if (category.getDatabaseId() == oldParent.getSubCategories().get(i).getDatabaseId())
            {
                oldParent.getSubCategories().remove(i);
                break;
            }
        }
        category.setId(newParent.getId() * 10 + newParent.getSubCategories().size()+1);
        newParent.getSubCategories().add(category);
    }

    public void publishChanges(ArrayList<Change> changes) throws SQLException
    {
        for(Change change: changes)
        {
            updateFather(change.getDatabaseId(), change.getParentDatabaseId());
        }
=======
    public void moveCategory(int databaseId, int parentDatabaseId) throws SQLException
    {
        updateFather(databaseId, parentDatabaseId);
>>>>>>> static-app/main
    }

    public Category findCategoryDatabaseId(int id, ArrayList<Category> categories)
    {
        for (Category category: categories)
        {
            if(category.getDatabaseId() == id)
                return category;
<<<<<<< HEAD

            Category temp = findCategoryDatabaseId(id, category.getSubCategories());
            if (temp != null)
=======
            Category temp = findCategoryDatabaseId(id, category.getSubCategories());
            if(temp != null)
>>>>>>> static-app/main
                return temp;
        }
        return null;
    }
<<<<<<< HEAD
=======

>>>>>>> static-app/main
    public boolean isParent(Category possibleChild, Category possibleParent)
    {
        return (Integer.toString(possibleChild.getId()).startsWith(Integer.toString(possibleParent.getId())));
    }

    public boolean alreadyExist(String name) throws SQLException
    {
        String query = "SELECT * FROM categories WHERE name = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                return resultSet.next();
            }
        }
    }
<<<<<<< HEAD

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
=======
>>>>>>> static-app/main
}