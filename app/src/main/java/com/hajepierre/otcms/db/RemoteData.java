package com.hajepierre.otcms.db;

/**
 * Created by jeanpierre on 5/1/18.
 */

public class RemoteData {
    private Integer id;
    private String name;

    public RemoteData() {
    }

    public RemoteData(String name) {
        this.name = name;
    }

    public RemoteData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
