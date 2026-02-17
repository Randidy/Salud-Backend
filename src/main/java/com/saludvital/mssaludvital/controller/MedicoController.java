package com.saludvital.mssaludvital.controller;

import com.saludvital.mssaludvital.dto.ApiResponse;
import com.saludvital.mssaludvital.dto.CitaResponse;
import com.saludvital.mssaludvital.entity.Medico;
import com.saludvital.mssaludvital.entity.Cita;
import com.saludvital.mssaludvital.service.CitaService;
import com.saludvital.mssaludvital.service.MedicoService;
import com.saludvital.mssaludvital.service.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medico")
@Tag(name = "Médicos", description = "Endpoints para gestión de médicos")
@SecurityRequirement(name = "bearerAuth")
public class MedicoController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/perfil")
    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Ver perfil del médico", description = "Obtiene la información del perfil del médico autenticado")
    public ResponseEntity<Medico> getPerfil(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Medico medico = medicoService.findByUsuarioId(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        // Devolver directamente el objeto Medico
        return ResponseEntity.ok(medico);
    }

    @GetMapping("/citas")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<?> getCitasMedico(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Medico medico = medicoService.findByUsuarioId(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        List<CitaResponse> citas = citaService.findByMedicoId(medico.getId())
                .stream()
                .map(this::mapToCitaResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "Citas obtenidas exitosamente", citas));
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Listar médicos disponibles", description = "Obtiene la lista de médicos activos y disponibles")
    public ResponseEntity<?> getMedicosDisponibles() {
        List<Medico> medicos = medicoService.findMedicosDisponibles();
        return ResponseEntity.ok(new ApiResponse(true, "Médicos disponibles obtenidos exitosamente", medicos));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los médicos", description = "Obtiene la lista completa de médicos (solo admin)")
    public ResponseEntity<?> getAllMedicos() {
        List<Medico> medicos = medicoService.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Médicos obtenidos exitosamente", medicos));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener médico por ID", description = "Obtiene la información de un médico específico")
    public ResponseEntity<?> getMedicoById(@PathVariable Long id) {
        Medico medico = medicoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + id));

        return ResponseEntity.ok(new ApiResponse(true, "Médico obtenido exitosamente", medico));
    }

    private CitaResponse mapToCitaResponse(Cita cita) {
        return new CitaResponse(
                cita.getId(),
                cita.getFecha(),
                cita.getHora(),
                cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido(),
                cita.getMedico().getNombreCompleto(),
                cita.getMedico().getEspecialidad().getDisplayName(),
                cita.getEstado(),
                cita.getMotivo(),
                cita.getTarifaAplicada()
        );
    }
}
