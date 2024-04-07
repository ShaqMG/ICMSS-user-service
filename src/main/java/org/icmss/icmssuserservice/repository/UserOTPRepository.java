package org.icmss.icmssuserservice.repository;

import org.icmss.icmssuserservice.domain.entity.UserOTP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserOTPRepository extends CrudRepository<UserOTP, String> {
}