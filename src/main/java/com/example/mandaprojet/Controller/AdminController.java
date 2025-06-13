package com.example.mandaprojet.Controller;


import com.example.mandaprojet.entity.BackofficeUser;
import com.example.mandaprojet.entity.BackofficeUser.UserRole;
import com.example.mandaprojet.Repositoty.BackofficeUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize; // Nécessaire pour @PreAuthorize

import java.util.List;

@Controller
@RequestMapping("/admin") // Toutes les URLs de ce contrôleur commencent par /admin
@PreAuthorize("hasRole('ADMIN')") // S'assure que seul un utilisateur avec le rôle ADMIN peut accéder à ce contrôleur
public class AdminController {

    private final BackofficeUserRepository userRepository;

    public AdminController(BackofficeUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Affiche la liste de tous les utilisateurs
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<BackofficeUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user_list"; // Renvoie vers templates/admin/user_list.html
    }

    // Affiche le formulaire de modification d'un utilisateur
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        BackofficeUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID utilisateur invalide: " + id));
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values()); // Passe tous les rôles pour le sélecteur
        return "admin/user_edit"; // Renvoie vers templates/admin/user_edit.html
    }

    // Traite la soumission du formulaire de modification d'utilisateur
    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute BackofficeUser userDetails, Model model) {
        BackofficeUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID utilisateur invalide: " + id));

        // Mettez à jour les champs que l'admin est autorisé à modifier
        user.setName(userDetails.getName());
        user.setFirstname(userDetails.getFirstname());
        user.setEmail(userDetails.getEmail());
        user.setIsActive(userDetails.getIsActive());
        user.setRole(userDetails.getRole()); // L'ADMIN peut changer le rôle ici
        user.setPosition(userDetails.getPosition()); // L'ADMIN peut changer la position ici

        // Ne pas mettre à jour le mot de passe ici à moins d'avoir un champ spécifique
        // et une logique pour le hacher si un nouveau mot de passe est entré.
        // Si vous voulez que l'admin puisse changer le mot de passe, ajoutez un champ
        // "newPassword" au formulaire et hachez-le AVANT de l'assigner à user.setPassword().

        userRepository.save(user);
        return "redirect:/admin/users?success=true"; // Redirige après succès
    }

    // Optionnel : Suppression d'un utilisateur
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users?deleted=true";
    }
}