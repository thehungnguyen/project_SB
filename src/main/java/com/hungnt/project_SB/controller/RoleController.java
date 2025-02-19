package com.hungnt.project_SB.controller;

import com.hungnt.project_SB.dto.request.RoleRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.RoleResponse;
import com.hungnt.project_SB.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/roles")
@RestController
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {
        return roleService.createRole(roleRequest);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<RoleResponse>> getAllRole() {
        return roleService.getAllRoles();
    }

    @DeleteMapping("/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteRole(@PathVariable String roleName) {
        return roleService.deleteRole(roleName);
    }
}
