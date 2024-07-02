package com.hungnt.project_SB.controller;

import com.hungnt.project_SB.dto.request.PermissionRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.PermissionResponse;
import com.hungnt.project_SB.entity.Permission;
import com.hungnt.project_SB.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/permissions")
@RestController
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest){
        Permission permission = permissionService.createPermission(permissionRequest);
        PermissionResponse permissionResponse = new PermissionResponse();
        ApiResponse apiResponse = new ApiResponse();

        permissionResponse.setName(permission.getName());
        permissionResponse.setDescription(permission.getDescription());

        apiResponse.setResult(permissionResponse);
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermission(){
        List<Permission> permissions = permissionService.getAllPermission();
        List<PermissionResponse> permissionResponses = new ArrayList<>();
        ApiResponse apiResponse = new ApiResponse();

        for(Permission p : permissions){
            PermissionResponse permissionResponse = new PermissionResponse();
            permissionResponse.setName(p.getName());
            permissionResponse.setDescription(p.getDescription());

            permissionResponses.add(permissionResponse);
        }

        apiResponse.setResult(permissionResponses);
        return apiResponse;
    }

    @DeleteMapping("/{permissionName}")
    public ApiResponse<String> deletePermission(@PathVariable String permissionName){
        permissionService.deletePermission(permissionName);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult("Permission has been deleted");
        return apiResponse;
    }
}
