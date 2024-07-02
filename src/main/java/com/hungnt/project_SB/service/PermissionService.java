package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.PermissionRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.PermissionResponse;
import com.hungnt.project_SB.entity.Permission;
import com.hungnt.project_SB.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public Permission createPermission(PermissionRequest permissionRequest){
        Permission permission = new Permission();

        permission.setName(permissionRequest.getName());
        permission.setDescription(permissionRequest.getDescription());

        return permissionRepository.save(permission);
    }

    public List<Permission> getAllPermission(){
        return permissionRepository.findAll();
    }

    public void deletePermission(String name){
        permissionRepository.deleteById(name);
    }
}
