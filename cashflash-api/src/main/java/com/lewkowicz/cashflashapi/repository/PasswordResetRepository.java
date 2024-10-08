package com.lewkowicz.cashflashapi.repository;

import com.lewkowicz.cashflashapi.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByResetPasswordToken(String resetToken);

}
