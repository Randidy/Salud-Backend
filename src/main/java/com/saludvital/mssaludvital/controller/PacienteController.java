package com.saludvital.mssaludvital.controller;

import com.saludvital.mssaludvital.dto.*;
import com.saludvital.mssaludvital.entity.*;
import com.saludvital.mssaludvital.enums.DiaSemana;
import com.saludvital.mssaludvital.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/paciente")
@Tag(name = "Pacientes", description = "Endpoints para gestión de pacientes y sus citas")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"}, allowCredentials = "true")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    
    @Autowired
    private RecetaService recetaService;
    
    @Autowired
    private CitaService citaService;

    // PERFIL Y EXPEDIENTE
    

    @GetMapping("/perfil")
    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Ver perfil del paciente", description = "Obtiene la información del perfil del paciente autenticado")
    public ResponseEntity<?> getPerfil(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Paciente paciente = pacienteService.findByUsuarioId(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        return ResponseEntity.ok(new ApiResponse(true, "Perfil obtenido exitosamente", paciente));
    }

    @GetMapping("/expediente")
    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Ver expediente médico del paciente", description = "Obtiene todos los datos médicos, recetas y citas del paciente autenticado")
    public ResponseEntity<?> getExpediente(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Paciente paciente = pacienteService.findByUsuarioId(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        List<String> alergias = paciente.getAlergias().stream()
                .map(Alergia::getNombre)
                .toList();
        List<String> enfermedades = paciente.getEnfermedades().stream()
                .map(Enfermedad::getNombre)
                .toList();

        List<Map<String, Object>> recetas = recetaService.findByPacienteId(paciente.getId()).stream()
                .map(receta -> {
                    Map<String, Object> recetaMap = new LinkedHashMap<>();
                    recetaMap.put("id", receta.getId());
                    recetaMap.put("fechaEmision", receta.getFechaEmision());
                    recetaMap.put("fechaCaducidad", receta.getFechaCaducidad());
                    recetaMap.put("medico", receta.getMedico().getNombreCompleto());
                    List<Map<String, Object>> items = receta.getItems().stream().map(item -> {
                        Map<String, Object> itemMap = new LinkedHashMap<>();
                        itemMap.put("medicamento", item.getMedicamento().getNombre());
                        itemMap.put("dosis", item.getDosis());
                        itemMap.put("frecuencia", item.getFrecuencia());
                        return itemMap;
                    }).toList();
                    recetaMap.put("items", items);
                    return recetaMap;
                }).toList();

        List<Map<String, Object>> citas = citaService.findByPacienteId(paciente.getId()).stream()
                .map(cita -> {
                    Map<String, Object> citaMap = new LinkedHashMap<>();
                    citaMap.put("id", cita.getId());
                    citaMap.put("fecha", cita.getFecha());
                    citaMap.put("hora", cita.getHora());
                    citaMap.put("medico", cita.getMedico().getNombreCompleto());
                    citaMap.put("especialidad", cita.getMedico().getEspecialidad().getDisplayName());
                    citaMap.put("estado", cita.getEstado());
                    citaMap.put("motivo", cita.getMotivo());
                    return citaMap;
                }).toList();

        Map<String, Object> expediente = new LinkedHashMap<>();
        expediente.put("id", paciente.getId());
        expediente.put("nombre", paciente.getNombre());
        expediente.put("apellido", paciente.getApellido());
        expediente.put("numeroIdentificacion", paciente.getNumeroIdentificacion());
        expediente.put("fechaNacimiento", paciente.getFechaNacimiento());
        expediente.put("edad", paciente.getEdad());
        expediente.put("telefono", paciente.getTelefono());
        expediente.put("direccion", paciente.getDireccion());
        expediente.put("alergias", alergias);
        expediente.put("enfermedades", enfermedades);
        expediente.put("recetas", recetas);
        expediente.put("citas", citas); 

        return ResponseEntity.ok(new ApiResponse(true, "Expediente obtenido exitosamente", expediente));
    }

   
    // 🔹 CITA → PACIENTE
    

    @GetMapping("/medicos")
    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Listar todos los médicos", description = "Obtiene la lista completa de médicos registrados, sin filtrar disponibilidad")
    public ResponseEntity<?> listarTodosLosMedicos() {
        List<Medico> medicos = medicoService.findAll();

        List<MedicoResponse> response = medicos.stream()
                .map(m -> new MedicoResponse(
                        m.getId(),
                        m.getNombreCompleto(),
                        m.getEspecialidad().getDisplayName()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "Médicos obtenidos exitosamente", response));
    }
    
    
    
    

    @GetMapping("/medicos/{id}")
    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Obtener médico por ID", description = "Obtiene la información de un médico específico")
    public ResponseEntity<MedicoResponse> obtenerMedicoPorId(@PathVariable Long id) {
        Medico medico = medicoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        MedicoResponse response = new MedicoResponse(
                medico.getId(),
                medico.getNombreCompleto(),
                medico.getEspecialidad().getDisplayName()
        );

        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/medicos-especialidad")
    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Listar médicos por especialidad", description = "Obtiene la lista completa de médicos con sus horarios agrupados por especialidad")
    public ResponseEntity<?> listarMedicoYEspecialidad() {
        List<Medico> medicos = medicoService.findAll();

        List<MedicoConHorariosResponse> response = medicos.stream()
            .map(m -> new MedicoConHorariosResponse(
                m.getId(),
                m.getNombreCompleto(),
                m.getEspecialidad().getDisplayName(),
                m.getHorarios().stream()
                    .map(h -> new HorarioResponse(
                        h.getDia().name(),
                        h.getHoraInicio().toString(),
                        h.getHoraFin().toString()
                    ))
                    .collect(Collectors.toList())
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "Médicos obtenidos exitosamente", response));
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    

    @PostMapping("/citas")
    @Operation(summary = "Registrar nueva cita", description = "Permite al paciente autenticado registrar una nueva cita")
    public ResponseEntity<?> crearCita(@Valid @RequestBody CitaRequest citaRequest, Authentication authentication) {
        // ⚠️ Validar si está autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "No autorizado. Inicia sesión nuevamente."));
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if (!userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, "Solo los pacientes pueden registrar citas"));
        }

        Paciente paciente = pacienteService.findByUsuarioId(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Medico medico = medicoService.findById(citaRequest.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        boolean yaExiste = citaService.existeCita(medico, citaRequest.getFecha(), citaRequest.getHora());
        if (yaExiste) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Ya existe una cita para ese médico en esa fecha y hora"));
        }

        long citasDelDia = citaService.findByPacienteId(paciente.getId()).stream()
                .filter(c -> c.getFecha().equals(citaRequest.getFecha()) && !c.getEstado().equals("CANCELADA"))
                .count();

        if (citasDelDia >= 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "No se pueden programar más de 3 citas en el mismo día"));
        }

        DiaSemana diaCita;
        try {
            diaCita = DiaSemana.fromString(citaRequest.getFecha().getDayOfWeek().name());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Día inválido para la cita: " + citaRequest.getFecha()));
        }

        boolean disponible = medico.getHorarios().stream().anyMatch(h ->
                h.getDia().equals(diaCita) &&
                !citaRequest.getHora().isBefore(h.getHoraInicio()) &&
                !citaRequest.getHora().isAfter(h.getHoraFin())
        );

        if (!disponible) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "El médico no atiende en ese día y hora"));
        }

        Cita cita = citaService.crearCita(
                paciente,
                medico,
                citaRequest.getFecha(),
                citaRequest.getHora(),
                citaRequest.getMotivo()
        );

        return ResponseEntity.ok(new ApiResponse(true, "Cita creada exitosamente", mapToResponse(cita)));
    }
    
    
    
    
    @GetMapping("/citas")
    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Listar citas del paciente", description = "Obtiene todas las citas del paciente autenticado")
    public ResponseEntity<?> getMisCitas(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Paciente paciente = pacienteService.findByUsuarioId(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        List<CitaResponse> responses = citaService.findByPacienteId(paciente.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "Citas obtenidas exitosamente", responses));
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
    
    
    
}
