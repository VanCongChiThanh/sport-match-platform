package com.sportmatch.profileservice.model;

import com.sportmatch.commonlibrary.model.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "profile")
public class Profile extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id",nullable = false, updatable = false,unique = true)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String fullName;

    private String phoneNumber;
    private String bio;
    private String profilePictureUrl;
    private String coverPictureUrl;
    private String location;

}