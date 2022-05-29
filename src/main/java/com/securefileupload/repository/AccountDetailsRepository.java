package com.securefileupload.repository;

import com.securefileupload.entity.AccountDetailsEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountDetailsRepository extends CrudRepository<AccountDetailsEntity, UUID> {
    @Query("select u from acc_dtls u where u.username = :username")
    public AccountDetailsEntity getUserByUsername(@Param("username") String username);
}
