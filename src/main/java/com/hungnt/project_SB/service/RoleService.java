package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.RoleRequest;
import com.hungnt.project_SB.entity.Role;
import com.hungnt.project_SB.repository.PermissionRepository;
import com.hungnt.project_SB.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public Role createRole(RoleRequest roleRequest){
        Role role = new Role();

        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());

        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        return roleRepository.save(role);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Role> getAllRole(){
        return roleRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRole(String name){
        roleRepository.deleteById(name);
    }
}
