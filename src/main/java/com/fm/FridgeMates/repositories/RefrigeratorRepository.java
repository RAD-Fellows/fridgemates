package com.fm.FridgeMates.repositories;

import com.fm.FridgeMates.models.ApplicationUser;
import com.fm.FridgeMates.models.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Long> {
}
