package com.saludvital.mssaludvital.config;

import com.saludvital.mssaludvital.security.JwtAuthenticationEntryPoint;
import com.saludvital.mssaludvital.security.JwtAuthenticationFilter;
import com.saludvital.mssaludvital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200", 
            "http://127.0.0.1:4200"
        ));
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        configuration.setAllowedHeaders(List.of("*"));
        
        configuration.setAllowCredentials(true);
        
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/api-docs/**").permitAll()
                
                // Admin endpoints - Solo ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Médico endpoints - Solo MEDICO 
                .requestMatchers("/medico/perfil").hasRole("MEDICO")
                .requestMatchers("/medico/disponibles").permitAll() 
                .requestMatchers("/medico/citas").hasRole("MEDICO")
                .requestMatchers("/recetas/medico").hasRole("MEDICO")
                .requestMatchers("/medico/{id}").authenticated() 
                .requestMatchers("/medico/**").hasAnyRole("ADMIN", "MEDICO")

                // Paciente endpoints - Solo PACIENTE
                .requestMatchers("/paciente/**").hasRole("PACIENTE")
                
                // Citas endpoints - Según funcionalidad
                .requestMatchers("/citas/nueva").hasRole("PACIENTE") 
                .requestMatchers("/citas/mis-citas").hasRole("PACIENTE") 
                .requestMatchers("/citas/medico").hasRole("MEDICO") 
                .requestMatchers("/citas/*/completar").hasAnyRole("MEDICO", "ADMIN")
                .requestMatchers("/citas/*/cancelar").hasAnyRole("PACIENTE", "ADMIN") 
                .requestMatchers("/citas/*").hasAnyRole("PACIENTE", "ADMIN") 
                .requestMatchers("/citas").hasRole("ADMIN") 
                
                // Recetas endpoints - Según funcionalidad
                .requestMatchers("/recetas/nueva").hasRole("MEDICO") 
                .requestMatchers("/recetas/mis-recetas").hasRole("PACIENTE") 
                .requestMatchers("/recetas/medico").hasRole("MEDICO") 
                .requestMatchers("/recetas").hasRole("ADMIN") 
                .requestMatchers("/recetas/*").hasAnyRole("PACIENTE", "MEDICO", "ADMIN") 
                
                // Medicamentos endpoints - Según método HTTP
                .requestMatchers(HttpMethod.GET, "/medicamentos/**").hasAnyRole("MEDICO", "ADMIN", "PACIENTE") 
                .requestMatchers(HttpMethod.POST, "/medicamentos").hasRole("ADMIN") 
                .requestMatchers(HttpMethod.PUT, "/medicamentos/*").hasRole("ADMIN") 
                .requestMatchers(HttpMethod.DELETE, "/medicamentos/*").hasRole("ADMIN") 
                
                
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

/**
 * Proyecto: Salud Vital
 * Autore: *
 *Andrea Fernanda Rebatta Atoche
 *Arom Amet Ortega Pacheco
 *Milagros Kiara Laime Povis
 *Hector Fernando Palacios Romero
 * Fecha: 24/10/2025
 */