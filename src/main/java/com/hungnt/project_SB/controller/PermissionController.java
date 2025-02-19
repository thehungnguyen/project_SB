package com.hungnt.project_SB.controller;

import com.hungnt.project_SB.dto.request.PermissionRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.PermissionResponse;
import com.hungnt.project_SB.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/permissions")
@RestController
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest) {
        return permissionService.createPermission(permissionRequest);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<PermissionResponse>> getAllPermission() {
        return permissionService.getAllPermissions();
    }

    @DeleteMapping("/{permissionName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deletePermission(@PathVariable String permissionName) {
        return permissionService.deletePermission(permissionName);
    }
}
