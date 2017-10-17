package id.co.sumi.transaku.model;

import java.io.Serializable;

public class RoleModel implements Serializable {
    private long id;
    private String name;
    private String description;
    private String authority;

    public RoleModel(long id, String name, String description, String authority) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.authority = authority;
    }

    public RoleModel(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
