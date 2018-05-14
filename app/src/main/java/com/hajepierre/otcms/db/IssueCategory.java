package com.hajepierre.otcms.db;

/**
 * Created by jeanpierre on 5/1/18.
 */

public class IssueCategory extends RemoteData {

    public IssueCategory() {
    }

    public IssueCategory(int id, String name) {
        super(id, name);
    }

    public IssueCategory(String name) {
        super(name);
    }
}
