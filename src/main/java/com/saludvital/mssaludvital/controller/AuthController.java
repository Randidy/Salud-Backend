package com.saludvital.mssaludvital.controller;

import com.saludvital.mssaludvital.dto.*;
import com.saludvital.mssaludvital.entity.Role;
import com.saludvital.mssaludvital.entity.User;
import com.saludvital.mssaludvital.entity.Alergia;
import com.saludvital.mssaludvital.entity.Enfermedad;
import com.saludvital.mssaludvital.entity.Paciente;
import com.saludvital.mssaludvital.enums.RoleName;
import com.saludvital.mssaludvital.repository.RoleRepository;
import com.saludvital.mssaludvital.security.JwtTokenProvider;
import com.saludvital.mssaludvital.service.UserService;
import com.saludvital.mssaludvital.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación y registro de usuarios")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    PacienteService pacienteService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autenticar usuario y obtener token JWT")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        
        User user = userService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
        
        UserInfoResponse userInfo = new UserInfoResponse(user.getId(), user.getName(), user.getEmail(), roles);
        
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, userInfo));
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar paciente", description = "Registrar nuevo paciente en el sistema (usuario + datos del paciente + alergias + enfermedades)")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "¡El email ya está en uso!"));
        }

        if (pacienteService.existsByNumeroIdentificacion(signUpRequest.getNumeroIdentificacion())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "¡El número de identificación ya está en uso!"));
        }

        User user = new User(
                signUpRequest.getNombre(), 
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        Role userRole = roleRepository.findByName(RoleName.ROLE_PACIENTE)
                .orElseThrow(() -> new RuntimeException("Rol PACIENTE no encontrado."));

        user.setRoles(Collections.singleton(userRole));
        User savedUser = userService.save(user);

        Paciente paciente = new Paciente(
                signUpRequest.getNombre(),
                signUpRequest.getApellido(),
                signUpRequest.getNumeroIdentificacion(),
                signUpRequest.getFechaNacimiento(),
                signUpRequest.getTelefono(),     
                signUpRequest.getDireccion()     
        );
        paciente.setUsuario(savedUser);

        if (signUpRequest.getAlergias() != null && !signUpRequest.getAlergias().isEmpty()) {
            Set<Alergia> alergias = signUpRequest.getAlergias().stream()
                    .map(nombre -> new Alergia(nombre, "", paciente))
                    .collect(Collectors.toSet());
            paciente.setAlergias(alergias);
        }

        if (signUpRequest.getEnfermedades() != null && !signUpRequest.getEnfermedades().isEmpty()) {
            Set<Enfermedad> enfermedades = signUpRequest.getEnfermedades().stream()
                    .map(nombre -> new Enfermedad(nombre, "", paciente))
                    .collect(Collectors.toSet());
            paciente.setEnfermedades(enfermedades);
        }

        pacienteService.save(paciente);

        return ResponseEntity.ok(new ApiResponse(true, "Paciente y usuario registrados exitosamente."));
    }
}