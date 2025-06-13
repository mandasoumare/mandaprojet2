package com.example.mandaprojet.Repositoty;


import com.example.mandaprojet.entity.BackofficeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BackofficeUserRepository extends JpaRepository<BackofficeUser, Long> {

    // Méthode pour trouver un utilisateur par son nom d'utilisateur
    Optional<BackofficeUser> findByUsername(String username);
    Optional<BackofficeUser> findByEmail(String email);
    boolean existsByUsername(String username); // Utile pour des vérifications booléennes
    boolean existsByEmail(String email);
    // Vous pouvez ajouter d'autres méthodes de recherche si nécessaire
    // Optional<BackofficeUser> findByEmail(String email);
    // boolean existsByUsername(String username);
    // boolean existsByEmail(String email);
}