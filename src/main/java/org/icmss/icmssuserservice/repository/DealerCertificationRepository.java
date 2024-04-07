package org.icmss.icmssuserservice.repository;

import org.icmss.icmssuserservice.domain.entity.DealerCertification;
import org.icmss.icmssuserservice.domain.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealerCertificationRepository extends JpaRepository<DealerCertification, Long> {

    @Query("""
            select  dc
            from DealerCertification dc
            where dc.userId = :userId
            and dc.dbStatus = :status
            """)
    Optional<DealerCertification> findByUserIdAndDbStatus(
            @Param("userId") String userId,
            @Param("status") DbStatus status
    );

    List<DealerCertification> findAllByDbStatus(DbStatus dbStatus);

    Optional<DealerCertification> findByIdAndDbStatus(Long id, DbStatus dbStatus);
}
