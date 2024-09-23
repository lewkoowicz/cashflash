package com.lewkowicz.cashflashapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferences extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPreferencesId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String defaultLanguage;

    private String defaultTheme;

}
