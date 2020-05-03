package org.gkk.bioshopapp.data.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {
    private String authority;

    public Role() {
    }

    public Role(String authority) {
        this.authority = authority;
    }

    @Column
    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return authority.equals(role.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority);
    }
}
