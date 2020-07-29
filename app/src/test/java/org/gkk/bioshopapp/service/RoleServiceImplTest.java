package org.gkk.bioshopapp.service;

import org.gkk.bioshopapp.base.TestBase;
import org.gkk.bioshopapp.data.model.Role;
import org.gkk.bioshopapp.data.repository.RoleRepository;
import org.gkk.bioshopapp.error.RoleNotFoundException;
import org.gkk.bioshopapp.service.service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RoleServiceImplTest extends TestBase {

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Test
    public void findByAuthority_whenRoleNotExist_ShouldThrowException() {
        String authority = UUID.randomUUID().toString();
        assertThrows(RoleNotFoundException.class, () -> roleService.findByAuthority(authority));
    }

    @Test
    public void findByAuthority_whenRoleExist_ShouldReturnRole() {
        String authority = "ROLE_ADMIN";
        String id = UUID.randomUUID().toString();

        Role role = new Role();
        role.setId(id);
        role.setAuthority(authority);

        Mockito.when(roleRepository.findByAuthority(authority)).thenReturn(Optional.of(role));

        Role roleDB = roleService.findByAuthority(authority);

        assertNotNull(roleDB);
        assertEquals(role.getId(), roleDB.getId());
        assertEquals(role.getAuthority(), roleDB.getAuthority());
    }

    @Test
    public void findAllRoles_shouldReturnAllRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ROLE_USER"));
        roles.add(new Role("ROLE_ADMIN"));
        roles.add(new Role("ROLE_ROOT"));

        Mockito.when(roleRepository.findAll()).thenReturn(roles);

        Set<Role> rolesDB = roleService.findAllRoles();

        assertEquals(3, rolesDB.size());
        assertTrue(rolesDB.contains(roles.get(0)));
        assertTrue(rolesDB.contains(roles.get(1)));
        assertTrue(rolesDB.contains(roles.get(2)));
    }

}