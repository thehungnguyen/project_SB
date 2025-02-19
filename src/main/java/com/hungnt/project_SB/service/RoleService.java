package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.RoleRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.RoleResponse;
import com.hungnt.project_SB.entity.Role;
import com.hungnt.project_SB.repository.PermissionRepository;
import com.hungnt.project_SB.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public ApiResponse<RoleResponse> createRole(RoleRequest roleRequest) {
        Role role = new Role();

        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());
        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        roleRepository.save(role);

        RoleResponse roleResponse = mapRoleMTR(role);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(roleResponse);
        return apiResponse;
    }

    public ApiResponse<List<RoleResponse>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleResponse> roleResponses = roles.stream().map(this::mapRoleMTR).toList();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(roleResponses);
        return apiResponse;
    }


    public ApiResponse<String> deleteRole(String name) {
        roleRepository.deleteById(name);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult("Role has been deleted");
        return apiResponse;
    }

    private RoleResponse mapRoleMTR(Role role) {
        RoleResponse roleResponse = new RoleResponse();

        roleResponse.setName(role.getName());
        roleResponse.setDescription(role.getDescription());
        roleResponse.setPermissions(role.getPermissions());

        return roleResponse;
    }
}
