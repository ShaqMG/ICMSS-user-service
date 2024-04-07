package org.icmss.icmssuserservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.icmss.icmssuserservice.domain.enums.DbStatus;
import org.icmss.icmssuserservice.domain.enums.UserRole;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @Column(updatable = false)
    private String id;

    @Column(unique = true)
    private String email;

    private String firstName;

    private String LastName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private BigDecimal balance;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private DbStatus status;

}
