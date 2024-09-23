package com.lewkowicz.cashflashapi.repository;

import com.lewkowicz.cashflashapi.entity.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {
}
