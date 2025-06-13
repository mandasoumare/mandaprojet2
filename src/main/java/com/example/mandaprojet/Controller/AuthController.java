package com.example.mandaprojet.Controller;


import com.example.mandaprojet.entity.BackofficeUser;
import com.example.mandaprojet.entity.BackofficeUser.UserRole;
import com.example.mandaprojet.Repositoty.BackofficeUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class AuthController {

    private final BackofficeUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(BackofficeUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- Connexion ---
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Nom d'utilisateur ou mot de passe incorrect.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Vous avez été déconnecté avec succès.");
        }
        return "auth/login"; // Renvoie vers templates/auth/login.html
    }

    // --- Inscription ---
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new BackofficeUser()); // Objet vide pour le formulaire
        model.addAttribute("roles", UserRole.values()); // Passe les rôles à la vue (pour le dropdown)
        return "auth/register"; // Renvoie vers templates/auth/register.html
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") BackofficeUser user, Model model) {
        // Vérifier si l'utilisateur existe déjà par username
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("registrationError", "Ce nom d'utilisateur est déjà pris.");
            return "auth/register";
        }
        // Vérifier si l'utilisateur existe déjà par email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("registrationError", "Cet email est déjà utilisé.");
            return "auth/register";
        }

        // Hacher le mot de passe avant de l'enregistrer
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(1); // Définir l'utilisateur comme actif par défaut
        user.setCreatedDate(LocalDateTime.now()); // Définir la date de création

        // *** ATTRIBUTION DU RÔLE PAR DÉFAUT ***
        user.setRole(UserRole.CCONTROL); // Attribue le rôle "CCONTROL" par défaut
        user.setPosition(1); // Attribue une position par défaut, par exemple 1

        userRepository.save(user);

        return "redirect:/login?registered=true";
    }

}
