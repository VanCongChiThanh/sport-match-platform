package com.sportmatch.identityservice.entity;

import com.sportmatch.identityservice.entity.enums.ActiveStatus;
import com.sportmatch.identityservice.entity.enums.AuthProvider;
import com.sportmatch.identityservice.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "username")
        }
)
public class User extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;
    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean emailVerified;

    @Column(nullable = false)
    private boolean locked;

    @Column
    private UUID confirmationToken;
    @Column
    private LocalDateTime confirmedAt;
    @Column
    private UUID forgotPasswordToken;
    @Column
    private UUID resetPasswordToken;
    private LocalDateTime forgotPasswordTokenExpiry;
    private LocalDateTime resetPasswordSentAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}