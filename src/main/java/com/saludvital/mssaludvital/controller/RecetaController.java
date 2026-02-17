package com.saludvital.mssaludvital.controller;

import com.saludvital.mssaludvital.dto.ApiResponse;
import com.saludvital.mssaludvital.dto.RecetaRequest;
import com.saludvital.mssaludvital.dto.RecetaResponse;
import com.saludvital.mssaludvital.entity.*;
import com.saludvital.mssaludvital.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recetas")
@Tag(name = "Recetas", description = "Endpoints para gestión de recetas médicas")
@SecurityRequirement(name = "bearerAuth")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private MedicamentoService medicamentoService;

    @PostMapping("/nueva")
    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Emitir nueva receta", description = "Crear una nueva receta médica")
    public ResponseEntity<?> crearReceta(@Valid @RequestBody RecetaRequest recetaRequest, 
                                        Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        Medico medico = medicoService.findByUsuarioId(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        
        Paciente paciente = pacienteService.findById(recetaRequest.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        
        List<RecetaService.ItemRecetaRequest> items = recetaRequest.getItems().stream()
                .map(item -> {
                    Medicamento medicamento = medicamentoService.findById(item.getMedicamentoId())
                            .orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));
                    return new RecetaService.ItemRecetaRequest(medicamento, item.getDosis(), item.getFrecuencia());
                })
                .collect(Collectors.toList());
        
        Receta receta = recetaService.crearReceta(paciente, medico, items);
        RecetaResponse response = mapToResponse(receta);
        
        return ResponseEntity.ok(new ApiResponse(true, "Receta creada exitosamente", response));
    }

    @GetMapping("/mis-recetas")
    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Ver mis recetas", description = "Obtiene las recetas del paciente autenticado")
    public ResponseEntity<?> getMisRecetas(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        Paciente paciente = pacienteService.findByUsuarioId(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        
        List<Receta> recetas = recetaService.findByPacienteId(paciente.getId());
        List<RecetaResponse> responses = recetas.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse(true, "Recetas obtenidas exitosamente", responses));
    }

    @GetMapping("/medico")
    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Ver recetas emitidas", description = "Obtiene las recetas emitidas por el médico autenticado")
    public ResponseEntity<?> getRecetasMedico(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Buscar el médico por usuarioId
        return medicoService.findByUsuarioId(userPrincipal.getId())
                .map(medico -> {
                    List<Receta> recetas = recetaService.findByMedicoId(medico.getId());
                    List<RecetaResponse> responses = recetas.stream()
                            .map(this::mapToResponse)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(new ApiResponse(true, "Recetas obtenidas exitosamente", responses));
                })
                .orElseGet(() -> ResponseEntity
                        .status(404)
                        .body(new ApiResponse(false, "No se encontró un médico asociado a este usuario", null))
                );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todas las recetas", description = "Obtiene todas las recetas del sistema (solo admin)")
    public ResponseEntity<?> getAllRecetas() {
        List<Receta> recetas = recetaService.findAll();
        List<RecetaResponse> responses = recetas.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse(true, "Recetas obtenidas exitosamente", responses));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver receta específica", description = "Obtiene los detalles de una receta específica")
    public ResponseEntity<?> getRecetaById(@PathVariable Long id) {
        Receta receta = recetaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con id: " + id));
        
        RecetaResponse response = mapToResponse(receta);
        
        return ResponseEntity.ok(new ApiResponse(true, "Receta obtenida exitosamente", response));
    }

    private RecetaResponse mapToResponse(Receta receta) {
        List<RecetaResponse.ItemRecetaResponse> items = receta.getItems().stream()
                .map(item -> new RecetaResponse.ItemRecetaResponse(
                        item.getMedicamento().getNombre(),
                        item.getDosis(),
                        item.getFrecuencia()
                ))
                .collect(Collectors.toList());

        return new RecetaResponse(
                receta.getId(),
                receta.getFechaEmision(),
                receta.getFechaCaducidad(),
                receta.getPaciente().getNombre(),
                receta.getMedico().getNombreCompleto(),
                items
        );
    }
}