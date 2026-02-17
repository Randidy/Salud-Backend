package com.saludvital.mssaludvital.controller;

import com.saludvital.mssaludvital.dto.*;
import com.saludvital.mssaludvital.entity.Alergia;
import com.saludvital.mssaludvital.entity.Cita;
import com.saludvital.mssaludvital.entity.Enfermedad;
import com.saludvital.mssaludvital.entity.HorarioAtencion;
import com.saludvital.mssaludvital.entity.Medico;
import com.saludvital.mssaludvital.entity.Paciente;
import com.saludvital.mssaludvital.entity.Role;
import com.saludvital.mssaludvital.entity.User;
import com.saludvital.mssaludvital.enums.DiaSemana;
import com.saludvital.mssaludvital.enums.Especialidad;
import com.saludvital.mssaludvital.enums.EstadoDoctor;
import com.saludvital.mssaludvital.enums.RoleName;
import com.saludvital.mssaludvital.repository.RoleRepository;
import com.saludvital.mssaludvital.service.CitaService;
import com.saludvital.mssaludvital.service.MedicoService;
import com.saludvital.mssaludvital.service.PacienteService;
import com.saludvital.mssaludvital.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Administración", description = "Endpoints para administración del sistema (solo administradores)")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    ///admin citas 
    @Autowired
    private CitaService citaService;
    
    

    // ------------------- ADMINISTRADORES -------------------
    @PostMapping("/usuarios")
    @Operation(summary = "Registrar nuevo administrador", description = "Crea un nuevo administrador en el sistema")
    public ResponseEntity<?> crearAdministrador(@Valid @RequestBody AdminRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "El email ya está en uso"));
        }

        User user = new User(request.getNombre(), request.getEmail(), request.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));
        user.setRoles(Collections.singleton(adminRole));

        User savedUser = userService.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "Administrador creado exitosamente", savedUser));
    }

    @GetMapping("/usuarios")
    @Operation(summary = "Listar administradores", description = "Obtiene la lista de todos los administradores")
    public ResponseEntity<?> listarAdministradores() {
        List<User> admins = userService.findAllByRoles_Name(RoleName.ROLE_ADMIN);
        return ResponseEntity.ok(new ApiResponse(true, "Administradores obtenidos exitosamente", admins));
    }

    @PutMapping("/usuarios/{id}")
    @Operation(summary = "Actualizar administrador", description = "Actualiza la información de un administrador por su ID")
    public ResponseEntity<?> actualizarAdministrador(@PathVariable Long id,
                                                     @Valid @RequestBody AdminUpdateRequest request) {
        User admin = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con ID: " + id));

        boolean esAdmin = admin.getRoles().stream()
                .anyMatch(r -> r.getName().equals(RoleName.ROLE_ADMIN));
        if (!esAdmin) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "El usuario no es un administrador"));
        }

        if (request.getNombre() != null) admin.setName(request.getNombre());
        if (request.getEmail() != null) admin.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userService.save(admin);

        return ResponseEntity.ok(new ApiResponse(true, "Administrador actualizado exitosamente", updatedUser));
    }

    @DeleteMapping("/usuarios/{id}")
    @Operation(summary = "Eliminar administrador", description = "Elimina un administrador por su ID")
    public ResponseEntity<?> eliminarAdministrador(@PathVariable Long id) {
        User admin = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con ID: " + id));

        boolean esAdmin = admin.getRoles().stream()
                .anyMatch(r -> r.getName().equals(RoleName.ROLE_ADMIN));
        if (!esAdmin) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "El usuario no es un administrador"));
        }

        userService.deleteById(id);

        return ResponseEntity.ok(new ApiResponse(true, "Administrador eliminado exitosamente"));
    }
    
    
 // ------------------- PACIENTES -------------------
    @GetMapping("/pacientes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar pacientes", description = "Obtiene todos los pacientes registrados en el sistema con alergias y enfermedades")
    public ResponseEntity<?> listarPacientes() {
        // Traer todos los pacientes con detalles
        List<Paciente> pacientes = pacienteService.findAllWithDetalles();

        return ResponseEntity.ok(new ApiResponse(true, "Pacientes obtenidos exitosamente", pacientes));
    }

    @GetMapping("/pacientes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener paciente por ID", description = "Permite obtener la información de un paciente por su ID")
    public ResponseEntity<?> obtenerPaciente(@PathVariable Long id) {
        Paciente paciente = pacienteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        return ResponseEntity.ok(new ApiResponse(true, "Paciente obtenido exitosamente", paciente));
    }

    // ------------------- EDITAR PACIENTE -------------------
    @PutMapping("/pacientes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar paciente", description = "Permite actualizar todos los datos de un paciente")
    public ResponseEntity<?> editarPaciente(@PathVariable Long id,
                                            @Valid @RequestBody SignUpRequest request) {

        Paciente paciente = pacienteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Paciente actualizado = pacienteService.actualizarPacienteCompleto(paciente, request);

        return ResponseEntity.ok(new ApiResponse(true, "Paciente actualizado exitosamente", actualizado));
    }

    // ------------------- ELIMINAR PACIENTE -------------------
    @DeleteMapping("/pacientes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar paciente", description = "Permite eliminar un paciente y su usuario asociado")
    public ResponseEntity<?> eliminarPaciente(@PathVariable Long id) {

        Paciente paciente = pacienteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

        if (paciente.getAlergias() != null) paciente.getAlergias().clear();
        if (paciente.getEnfermedades() != null) paciente.getEnfermedades().clear();

        pacienteService.deleteById(id);

        userService.deleteById(paciente.getUsuario().getId());

        return ResponseEntity.ok(new ApiResponse(true, "Paciente eliminado exitosamente"));
    }

    // ------------------- REGISTRAR PACIENTE POR ADMIN -------------------
    @PostMapping("/pacientes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Registrar paciente por admin", description = "Permite a un administrador registrar un nuevo paciente")
    public ResponseEntity<?> adminRegisterPaciente(@Valid @RequestBody SignUpRequest signUpRequest) {

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "¡El email ya está en uso!"));
        }
        if (pacienteService.existsByNumeroIdentificacion(signUpRequest.getNumeroIdentificacion())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "¡El número de identificación ya está en uso!"));
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

        Set<Alergia> alergias = signUpRequest.getAlergias() != null
                ? signUpRequest.getAlergias().stream()
                    .map(nombre -> {
                        Alergia a = new Alergia();
                        a.setNombre(nombre);
                        return a;
                    })
                    .collect(Collectors.toSet())
                : null;

        Set<Enfermedad> enfermedades = signUpRequest.getEnfermedades() != null
                ? signUpRequest.getEnfermedades().stream()
                    .map(nombre -> {
                        Enfermedad e = new Enfermedad();
                        e.setNombre(nombre);
                        return e;
                    })
                    .collect(Collectors.toSet())
                : null;

        Paciente paciente = pacienteService.createPaciente(
                signUpRequest.getNombre(),
                signUpRequest.getApellido(),
                signUpRequest.getNumeroIdentificacion(),
                signUpRequest.getFechaNacimiento(),
                savedUser,
                alergias,
                enfermedades,
                signUpRequest.getTelefono(),
                signUpRequest.getDireccion()
        );

        return ResponseEntity.ok(new ApiResponse(true, "Paciente registrado exitosamente por ADMIN."));
    }
    
    
 // ------------------- MÉDICOS -------------------
    @PostMapping("/medicos")
    @Operation(summary = "Registrar nuevo médico", description = "Crea un nuevo médico en el sistema con rol MEDICO")
    public ResponseEntity<?> crearMedico(@Valid @RequestBody MedicoRequest request) {

        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "El email ya está en uso"));
        }
        if (medicoService.existsByNumeroLicencia(request.getNumeroLicencia())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "El número de licencia ya está en uso"));
        }

        User user = new User(
                request.getNombre() + " " + request.getApellido(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        Role medicoRole = roleRepository.findByName(RoleName.ROLE_MEDICO)
                .orElseThrow(() -> new RuntimeException("Rol MEDICO no encontrado"));
        user.setRoles(Collections.singleton(medicoRole));
        User savedUser = userService.save(user);

        List<HorarioAtencion> horarios = new ArrayList<>();
        if (request.getHorarios() != null) {
            for (HorarioRequest h : request.getHorarios()) {
                if (h.getDia() == null || h.getDia().trim().isEmpty()) {
                    return ResponseEntity.badRequest().body(new ApiResponse(false, "Día del horario no puede ser nulo o vacío"));
                }

                HorarioAtencion ha = new HorarioAtencion();
                try {
                    ha.setDia(DiaSemana.fromString(h.getDia().trim()));
                    ha.setHoraInicio(LocalTime.parse(h.getHoraInicio()));
                    ha.setHoraFin(LocalTime.parse(h.getHoraFin()));
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body(new ApiResponse(false, "Horario inválido: " + e.getMessage()));
                }

                if (ha.getHoraInicio().isAfter(ha.getHoraFin())) {
                    return ResponseEntity.badRequest().body(new ApiResponse(false, "Hora de inicio debe ser antes de hora fin"));
                }

                horarios.add(ha);
            }
        }

        // Crear medico
        Medico medico = medicoService.createMedico(
                request.getNombre(),
                request.getApellido(),
                request.getNumeroLicencia(),
                request.getTelefono(),
                request.getEmail(),
                request.getEspecialidad(),
                request.getTarifaConsulta(),
                savedUser,
                horarios
        );

        return ResponseEntity.ok(new ApiResponse(true, "Médico creado exitosamente", medico));
    }

 // ------------------- LISTAR -------------------
    @GetMapping("/medicos")
    @Operation(summary = "Listar médicos", description = "Obtiene todos los médicos registrados en el sistema")
    public ResponseEntity<List<Medico>> listarMedicos() {
        List<Medico> medicos = medicoService.findAll();  
        return ResponseEntity.ok(medicos);
    }

    // ------------------- OBTENER POR ID -------------------
    @GetMapping("/medicos/{id}")
    public ResponseEntity<Medico> getMedicoById(@PathVariable Long id) {
        Medico medico = medicoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + id));
        return ResponseEntity.ok(medico);
    }
    


 // ------------------- EDITAR -------------------
    @PutMapping("/medicos/{id}")
    @Operation(summary = "Actualizar médico", description = "Edita los datos de un médico existente")
    public ResponseEntity<?> actualizarMedico(
            @PathVariable Long id,
            @Valid @RequestBody MedicoUpdateRequest request) {

        try {
            List<HorarioAtencion> horarios = new ArrayList<>();
            if (request.getHorarios() != null) {
                for (HorarioRequest h : request.getHorarios()) {
                    if (h.getDia() == null || h.getDia().trim().isEmpty()) {
                        return ResponseEntity.badRequest().body(new ApiResponse(false, "Día del horario no puede ser nulo o vacío"));
                    }

                    HorarioAtencion ha = new HorarioAtencion();
                    ha.setDia(DiaSemana.fromString(h.getDia().trim()));
                    ha.setHoraInicio(LocalTime.parse(h.getHoraInicio()));
                    ha.setHoraFin(LocalTime.parse(h.getHoraFin()));

                    if (ha.getHoraInicio().isAfter(ha.getHoraFin())) {
                        return ResponseEntity.badRequest().body(new ApiResponse(false, "Hora de inicio debe ser antes de hora fin"));
                    }

                    horarios.add(ha);
                }
            }

            Medico actualizado = medicoService.updateMedico(
                    id,
                    request.getNombre(),
                    request.getApellido(),
                    request.getNumeroLicencia(),
                    request.getTelefono(),
                    request.getEmail(),
                    request.getEspecialidad(),
                    request.getTarifaConsulta(),
                    request.getEstado(),
                    request.getDisponible(),
                    horarios
            );

            return ResponseEntity.ok(new ApiResponse(true, "Médico actualizado exitosamente", actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }


    // ------------------- ELIMINAR -------------------
    @DeleteMapping("/medicos/{id}")
    @Operation(summary = "Desactivar médico", description = "Cambia el estado del médico a INACTIVO en lugar de eliminarlo")
    public ResponseEntity<?> desactivarMedico(@PathVariable Long id) {
        Medico medico = medicoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + id));

        // Cambiar estado
        medico.setEstado(EstadoDoctor.INACTIVO);
        medicoService.save(medico);

        return ResponseEntity.ok(new ApiResponse(true, "Médico desactivado exitosamente"));
    }
    // ------------------- COMBOS (DÍAS Y ESPECIALIDADES) -------------------
    @GetMapping("/medicos/dias")
    @Operation(summary = "Listar días de la semana", description = "Obtiene todos los días definidos en el enum DiaSemana")
    public ResponseEntity<List<String>> getDiasSemana() {
        List<String> dias = Arrays.stream(DiaSemana.values())
                                  .map(Enum::name)
                                  .collect(Collectors.toList());
        return ResponseEntity.ok(dias);
    }

    @GetMapping("/medicos/especialidades")
    @Operation(summary = "Listar especialidades médicas", description = "Obtiene todas las especialidades definidas en el enum Especialidad")
    public ResponseEntity<List<String>> getEspecialidades() {
        List<String> especialidades = Arrays.stream(Especialidad.values())
                                            .map(Enum::name)
                                            .collect(Collectors.toList());
        return ResponseEntity.ok(especialidades);
    }
    
    
    @GetMapping("/citas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCitas() {
        List<CitaResponse> responses = citaService.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "Todas las citas obtenidas exitosamente", responses));
    }

    private CitaResponse mapToResponse(Cita cita) {
        return new CitaResponse(
                cita.getId(),
                cita.getFecha(),
                cita.getHora(),
                cita.getPaciente().getNombre(),
                cita.getMedico().getNombreCompleto(),
                cita.getMedico().getEspecialidad().getDisplayName(),
                cita.getEstado(),
                cita.getMotivo(),
                cita.getTarifaAplicada()
        );
    }
    
    @PutMapping("/citas/{id}/cancelar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cancelar cita", description = "Permite al administrador cancelar una cita específica")
    public ResponseEntity<?> cancelarCita(@PathVariable Long id) {
        try {
            citaService.cancelarCita(id);
            return ResponseEntity.ok(new ApiResponse(true, "Cita cancelada exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/citas/{id}/completar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Completar cita", description = "Permite al administrador marcar una cita como completada")
    public ResponseEntity<?> completarCita(@PathVariable Long id) {
        try {
            citaService.completarCita(id);
            return ResponseEntity.ok(new ApiResponse(true, "Cita marcada como completada"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/citas/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar cita", description = "Permite al administrador eliminar una cita definitivamente del sistema")
    public ResponseEntity<?> eliminarCita(@PathVariable Long id) {
        try {
            citaService.eliminarCita(id);
            return ResponseEntity.ok(new ApiResponse(true, "Cita eliminada exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
    

}
