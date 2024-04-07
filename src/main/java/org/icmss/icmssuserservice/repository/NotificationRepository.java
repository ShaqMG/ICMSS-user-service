package org.icmss.icmssuserservice.repository;

import org.icmss.icmssuserservice.domain.entity.Notification;
import org.icmss.icmssuserservice.domain.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdAndStatus(String userId, DbStatus status);

    Optional<Notification> findByIdAndStatus(Long id, DbStatus status);
}
