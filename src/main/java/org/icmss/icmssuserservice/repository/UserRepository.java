package org.icmss.icmssuserservice.repository;

import org.icmss.icmssuserservice.domain.entity.Users;
import org.icmss.icmssuserservice.domain.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("""
        select u
        from Users u
        where u.status = :status
        and u.id = :userId
    """)
    Optional<Users> getUserById(
            @Param("userId") String userId,
            @Param("status") DbStatus status
    );

    @Query("""
        select u
        from Users u
        where u.status = :status
        and u.email = :email
    """)
    Optional<Users> getUserByEmailAndStatus(
            @Param("email") String email,
            @Param("status") DbStatus status
    );
}
