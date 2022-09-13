package it.polimi.tiw.beans;

public class Change
{
    private Integer categoryId;
    private Integer databaseId;
    private Integer parentId;
    private Integer parentDatabaseId;

    public Change(Integer categoryId, Integer databaseId, Integer parentId, Integer parentDatabaseId)
    {
        this.categoryId = categoryId;
        this.databaseId = databaseId;
        this.parentId = parentId;
        this.parentDatabaseId = parentDatabaseId;
    }
    public Integer getCategoryId() {
        return categoryId;
    }

    public Integer getDatabaseId() {
        return databaseId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getParentDatabaseId() {
        return parentDatabaseId;
    }
}
