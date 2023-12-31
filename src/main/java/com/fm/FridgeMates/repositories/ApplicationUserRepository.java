package com.fm.FridgeMates.repositories;

import com.fm.FridgeMates.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    public ApplicationUser findByUsername(String username);

    List<ApplicationUser> findByCityAndIdNot(String city, Long id);

}
