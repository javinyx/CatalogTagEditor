package it.polimi.tiw.tiw_project_2020_21.beans;

import java.util.ArrayList;
import java.util.Date;

public class Category
{
    private int id;
    private int databaseId;
    private String name;
    private ArrayList<Category> subCategories = new ArrayList<>();

    public Category(int id, String name, int databaseId) {
        this.id = id;
        this.name = name;
        this.databaseId = databaseId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getDatabaseId() {
        return databaseId;
    }
    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<Category> getSubCategories() {
        return subCategories;
    }
    public void setSubCategories(ArrayList<Category> subCategories) {
        this.subCategories = subCategories;
    }
}
