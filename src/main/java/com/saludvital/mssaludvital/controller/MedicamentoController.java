package com.saludvital.mssaludvital.controller;

import com.saludvital.mssaludvital.dto.ApiResponse;
import com.saludvital.mssaludvital.entity.Medicamento;
import com.saludvital.mssaludvital.service.MedicamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicamentos")
@Tag(name = "Medicamentos", description = "Endpoints para gestión de medicamentos")
@SecurityRequirement(name = "bearerAuth")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @GetMapping
    @Operation(summary = "Listar medicamentos", description = "Obtiene la lista de todos los medicamentos")
    public ResponseEntity<?> getAllMedicamentos() {
        List<Medicamento> medicamentos = medicamentoService.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Medicamentos obtenidos exitosamente", medicamentos));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener medicamento por ID", description = "Obtiene un medicamento específico por su ID")
    public ResponseEntity<?> getMedicamentoById(@PathVariable Long id) {
        Medicamento medicamento = medicamentoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con id: " + id));
        
        return ResponseEntity.ok(new ApiResponse(true, "Medicamento obtenido exitosamente", medicamento));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar medicamentos", description = "Busca medicamentos por nombre")
    public ResponseEntity<?> buscarMedicamentos(@RequestParam String nombre) {
        List<Medicamento> medicamentos = medicamentoService.findByNombreContaining(nombre);
        return ResponseEntity.ok(new ApiResponse(true, "Medicamentos encontrados exitosamente", medicamentos));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear medicamento", description = "Registra un nuevo medicamento (solo admin)")
    public ResponseEntity<?> crearMedicamento(@Valid @RequestBody Medicamento medicamentoRequest) {
        Medicamento medicamento = medicamentoService.crearMedicamento(
                medicamentoRequest.getNombre(),
                medicamentoRequest.getDescripcion()
        );
        
        return ResponseEntity.ok(new ApiResponse(true, "Medicamento creado exitosamente", medicamento));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar medicamento", description = "Actualiza un medicamento existente (solo admin)")
    public ResponseEntity<?> actualizarMedicamento(@PathVariable Long id, 
                                                  @Valid @RequestBody Medicamento medicamentoRequest) {
        Medicamento medicamento = medicamentoService.actualizarMedicamento(
                id,
                medicamentoRequest.getNombre(),
                medicamentoRequest.getDescripcion()
        );
        
        return ResponseEntity.ok(new ApiResponse(true, "Medicamento actualizado exitosamente", medicamento));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar medicamento", description = "Elimina un medicamento del sistema (solo admin)")
    public ResponseEntity<?> eliminarMedicamento(@PathVariable Long id) {
        medicamentoService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(true, "Medicamento eliminado exitosamente"));
    }
}