package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.data.model.Role;

import java.util.Set;

public interface RoleService {

    void seedRolesInDb();

    Set<Role> findAllRoles();

    Role findByAuthority(String authority);
}
