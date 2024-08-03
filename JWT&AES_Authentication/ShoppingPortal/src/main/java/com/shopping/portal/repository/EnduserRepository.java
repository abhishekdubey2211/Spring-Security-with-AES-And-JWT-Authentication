package com.shopping.portal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopping.portal.model.EndUser;


@Repository
public interface EnduserRepository extends JpaRepository<EndUser, Long> {

    Optional<EndUser> findByUsername(String username);

    Optional<EndUser> findByEmail(String email);

    List<EndUser> findByRole(String role);
}
