package org.gkk.bioshopapp.web.view.model.user;

import org.gkk.bioshopapp.data.model.Role;

import java.util.Set;

public class UserProfileViewModel {

    private String id;

    private String username;

    private String email;

    private Set<Role> authorities;

    public UserProfileViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }
}
