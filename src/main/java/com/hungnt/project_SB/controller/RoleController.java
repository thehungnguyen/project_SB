package com.hungnt.project_SB.controller;

import com.hungnt.project_SB.dto.request.RoleRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.dto.response.RoleResponse;
import com.hungnt.project_SB.entity.Role;
import com.hungnt.project_SB.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/roles")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest){
        Role role = roleService.createRole(roleRequest);
        RoleResponse roleResponse = new RoleResponse();
        ApiResponse apiResponse = new ApiResponse();

        roleResponse.setName(role.getName());
        roleResponse.setDescription(role.getDescription());
        roleResponse.setPermissions(role.getPermissions());

        apiResponse.setResult(roleResponse);
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRole(){
        List<Role> roles = roleService.getAllRole();
        List<RoleResponse> roleResponses = new ArrayList<>();
        ApiResponse apiResponse = new ApiResponse();

        for(Role r : roles){
            RoleResponse roleResponse = new RoleResponse();

            roleResponse.setName(r.getName());
            roleResponse.setDescription(r.getDescription());
            roleResponse.setPermissions(r.getPermissions());

            roleResponses.add(roleResponse);
        }

        apiResponse.setResult(roleResponses);
        return apiResponse;
    }

    @DeleteMapping("/{roleName}")
    public ApiResponse<String> deleteRole(@PathVariable String roleName){
        roleService.deleteRole(roleName);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult("Role has been deleted");
        return apiResponse;
    }
}
