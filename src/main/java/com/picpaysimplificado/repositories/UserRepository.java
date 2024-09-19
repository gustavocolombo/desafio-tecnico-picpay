package com.picpaysimplificado.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.picpaysimplificado.domain.user.User;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, UUID>{
  Optional<User> findByDocument(String document);
}
