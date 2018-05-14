package com.hajepierre.otcms.db;

/**
 * Created by jeanpierre on 5/1/18.
 */

public class Nationality extends RemoteData {

    public Nationality() {
    }

    public Nationality(String name) {
        super(name);
    }

    public Nationality(int id, String name) {
        super(id, name);
    }
}
