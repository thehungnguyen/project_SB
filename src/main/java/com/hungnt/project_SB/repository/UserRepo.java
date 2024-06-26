package com.hungnt.project_SB.repository;

import com.hungnt.project_SB.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String>{
    boolean existsByUsername(String username);
}
