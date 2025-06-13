package com.example.mandaprojet.config;


import com.example.mandaprojet.entity.BackofficeUser;
import com.example.mandaprojet.Repositoty.BackofficeUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final BackofficeUserRepository userRepository;

    public SecurityConfig(BackofficeUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. PasswordEncoder: Pour hacher les mots de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt est fortement recommandé pour le hachage des mots de passe
    }

    // 2. UserDetailsService: Pour charger les détails de l'utilisateur depuis la base de données
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            BackofficeUser user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

            // Crée un UserDetails de Spring Security à partir de notre BackofficeUser
            // Les rôles Spring Security doivent être préfixés par "ROLE_"
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword()) // Le mot de passe doit être haché
                    .roles(user.getRole().name()) // Convertit l'enum UserRole en String (ex: "ADMIN")
                    .build();
        };
    }

    // 3. SecurityFilterChain: Configure les règles d'autorisation HTTP et le formulaire de connexion
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register", "/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Les pages de prédiction sont accessibles à tous les utilisateurs connectés
                        .requestMatchers("/predictions/**").hasAnyRole("CCONTROL", "ICONTROL", "PCONTROL", "ADMIN")

                        // MODIFICATION ICI : Permettre aux rôles non-admin de voir la liste des suspects
                        // Si seuls CCONTROL, ICONTROL, PCONTROL et ADMIN doivent voir la liste, utilisez hasAnyRole
                        .requestMatchers("/suspects/**").hasAnyRole("CCONTROL", "ICONTROL", "PCONTROL", "ADMIN")
                        // Si tous les utilisateurs authentifiés doivent la voir, utilisez .authenticated()
                        // .requestMatchers("/suspects/**").authenticated()

                        // La page d'ajout reste réservée aux ADMINs
                        .requestMatchers("/add/**").hasRole("ADMIN")

                        // Toute autre requête nécessite une authentification
                        .anyRequest().authenticated()
                )
                // ... reste de la configuration (formLogin, logout, exceptionHandling)
                .formLogin(form -> form
                        .loginPage("/login") // Spécifie notre page de connexion personnalisée
                        .loginProcessingUrl("/authenticateTheUser") // URL à laquelle le formulaire de connexion sera soumis
                        .defaultSuccessUrl("/suspects", true) // Redirige vers les prédictions après connexion réussie
                        .failureUrl("/login?error=true") // Redirige vers la page de connexion avec un paramètre d'erreur
                        .permitAll() // Autorise l'accès à la page de connexion
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL pour se déconnecter
                        .logoutSuccessUrl("/login?logout=true") // Redirige après déconnexion
                        .permitAll()
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/access-denied") // Page personnalisée pour les erreurs d'accès refusé
                );

        return http.build();
    }

    // Bean optionnel pour gérer la redirection après une connexion réussie
    // (Utile si vous voulez une logique plus complexe que defaultSuccessUrl)
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler("/suspects");

    }
    // --- NOUVEAU BEAN POUR ACTIVER LES EXPRESSIONS DE SÉCURITÉ DANS THYMELEAF ---
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}