package com.lewkowicz.cashflashapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
public class PasswordReset extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passwordResetId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String resetPasswordToken;

    private LocalDateTime resetPasswordTokenExpiryDate;

}
