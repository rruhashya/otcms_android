package com.hajepierre.otcms.db;

/**
 * Created by jeanpierre on 5/1/18.
 */

public class Issue extends RemoteData
{

    private int categoryId;

    public Issue()
    {

    }

    public Issue(String name, int categoryId)
    {

        super(name);
        this.categoryId=categoryId;
    }

    public Issue(int id, String name, int categoryId)
    {

        super(id, name);
        this.categoryId = categoryId;
    }

    public Issue(int id, String name)
    {

        super(id, name);
    }

    public int getCategoryId()
    {

        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {

        this.categoryId = categoryId;
    }
}
