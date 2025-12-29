package com.sportmatch.identityservice.repository;


import com.sportmatch.identityservice.entity.User;
import com.sportmatch.identityservice.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Set<User> findAllByIdIn(Set<UUID> ids);

    boolean existsByEmail(String email);

    Optional<User> findByConfirmationToken(UUID confirmationToken);

    List<User> findAllByRole(Role role);

    @Modifying
    @Query(
            nativeQuery = true,
            value =
                    "update oauth_access_tokens set revoked_at = now() where user_id =:user_id and revoked_at isnull")
    void revokeAll(@Param("user_id") UUID userId);

    @Query(
            value =
                    "SELECT u.* "
                            + "FROM users u inner join user_in_room c  "
                            + "on c.user_id=u.id "
                            + "WHERE c.room_id=:roomId",
            nativeQuery = true)
    Page<User> getListUserByRoomId(Pageable pageable, UUID roomId);

    @Query(
            value = "SELECT u.* "
                    + "FROM users u "
                    + "WHERE u.email LIKE %:email% AND u.role='ROLE_USER'",
            nativeQuery = true)
    Page<User> searchByEmail(Pageable pageable, String email);
}