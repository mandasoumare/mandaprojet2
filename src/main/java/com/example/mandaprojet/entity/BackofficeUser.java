package com.example.mandaprojet.entity;

import jakarta.persistence.*; // Utilisez jakarta.persistence pour Spring Boot 3+
import lombok.Data; // Nécessite Lombok
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "backofficeusers") // Nom exact de votre table
@Data // Génère getters, setters, toString, equals, hashCode (via Lombok)
@NoArgsConstructor // Génère un constructeur sans arguments (via Lombok)
@AllArgsConstructor // Génère un constructeur avec tous les arguments (via Lombok)
public class BackofficeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT en MySQL
    @Column(name = "ID")
    private Long id; // Utilisez Long pour les ID auto-incrémentés

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "FIRSTNAME", nullable = false)
    private String firstname;

    @Column(name = "USERNAME", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "PASSWORD", nullable = false) // Attention : la colonne est "PASSWORT" dans votre DB
    private String password; // Nom du champ Java sera "password"

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "ISACTIV", nullable = false)
    private Integer isActive; // 0 pour inactif, 1 pour actif (ou un booléen si vous préférez)

    @Column(name = "CREATED_DATE", nullable = false)
    private LocalDateTime createdDate;

    // Pour l'ENUM 'ALLOW'
    @Enumerated(EnumType.STRING) // Indique à JPA de stocker le nom de l'enum (CCONTROL, ADMIN, etc.)
    @Column(name = "ALLOW", nullable = false)
    private UserRole role; // Le nom du champ sera 'role'

    @Column(name = "POSITION", nullable = false)
    private Integer position; // La colonne "POSITION" dans votre DB

    // Définition de l'Enum UserRole
    public enum UserRole {
        CCONTROL,
        ICONTROL,
        PCONTROL,
        ADMIN
    }
}