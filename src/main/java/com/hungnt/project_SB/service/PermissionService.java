package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.PermissionRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.PermissionResponse;
import com.hungnt.project_SB.entity.Permission;
import com.hungnt.project_SB.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public ApiResponse<PermissionResponse> createPermission(PermissionRequest permissionRequest) {
        Permission permission = new Permission();

        permission.setName(permissionRequest.getName());
        permission.setDescription(permissionRequest.getDescription());
        permissionRepository.save(permission);

        PermissionResponse permissionResponse = mapPermissionMTR(permission);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(permissionResponse);
        return apiResponse;
    }


    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        List<PermissionResponse> permissionResponses = permissions.stream().map(this::mapPermissionMTR).toList();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(permissionResponses);
        return apiResponse;
    }


    public ApiResponse<String> deletePermission(String name) {
        permissionRepository.deleteById(name);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult("Permission has been deleted");
        return apiResponse;
    }

    private PermissionResponse mapPermissionMTR(Permission permission) {
        PermissionResponse permissionResponse = new PermissionResponse();

        permissionResponse.setName(permission.getName());
        permissionResponse.setDescription(permission.getDescription());

        return permissionResponse;
    }
}
