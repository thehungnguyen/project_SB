package com.hungnt.project_SB.dto.request;

import com.hungnt.project_SB.entity.Permission;

import java.util.Set;

public class RoleRequest {
    private String name;
    private String description;
    private Set<String> permissions;

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

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
