package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.Role;
import org.gkk.bioshopapp.data.repository.RoleRepository;
import org.gkk.bioshopapp.error.RoleNotFoundException;
import org.gkk.bioshopapp.service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void seedRolesInDb() {
        if (this.roleRepository.count() == 0){
            this.roleRepository.saveAndFlush(new Role("ROLE_USER"));
            this.roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
            this.roleRepository.saveAndFlush(new Role("ROLE_ROOT"));
        }
    }

    @Override
    public Set<Role> findAllRoles() {
        return new HashSet<>(this.roleRepository.findAll());
    }

    @Override
    public Role findByAuthority(String authority) {
        return this.roleRepository.findByAuthority(authority)
                .orElseThrow(()->new RoleNotFoundException("Role not found!"));
    }
}
