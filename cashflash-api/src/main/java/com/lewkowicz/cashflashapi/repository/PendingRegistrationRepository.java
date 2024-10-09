package com.lewkowicz.cashflashapi.repository;

import com.lewkowicz.cashflashapi.entity.PendingRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingRegistrationRepository extends JpaRepository<PendingRegistration, Long> {

    Optional<PendingRegistration> findByEmail(String email);

    Optional<PendingRegistration> findByConfirmationToken(String token);

}
