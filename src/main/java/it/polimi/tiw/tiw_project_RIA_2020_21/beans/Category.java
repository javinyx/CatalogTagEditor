package it.polimi.tiw.tiw_project_RIA_2020_21.beans;

import java.util.ArrayList;
import java.util.Date;

public class Category
{
    private int id;
    private int level;
    private String name;
    private Date lastModifiedDate;
    private ArrayList<Category> subCategories = new ArrayList<>();

    public Category(int id, int level, String name) {
        this.id = id;
        this.level = level;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public int getLevel()
    {
        return level;
    }
    public void setLevel(int level)
    {
        this.level = level;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public ArrayList<Category> getSubCategories() {
        return subCategories;
    }
    public void setSubCategories(ArrayList<Category> subCategories) {
        this.subCategories = subCategories;
    }
}
