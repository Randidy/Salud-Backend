package com.saludvital.mssaludvital.controller;


import com.saludvital.mssaludvital.dto.EnfermedadRequest;
import com.saludvital.mssaludvital.entity.Enfermedad;
import com.saludvital.mssaludvital.entity.Paciente;
import com.saludvital.mssaludvital.repository.EnfermedadRepository;
import com.saludvital.mssaludvital.repository.PacienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/enfermedades")
public class EnfermedadController {

    @Autowired
    private EnfermedadRepository enfermedadRepository;

    @Autowired
    private PacienteRepository pacienteRepository;
    
    @PostMapping
    public ResponseEntity<Enfermedad> crearEnfermedad(@Valid @RequestBody EnfermedadRequest request) {
        Enfermedad enfermedad = new Enfermedad();
        enfermedad.setNombre(request.getNombre());
        enfermedad.setDescripcion(request.getDescripcion());

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        enfermedad.setPaciente(paciente);

        enfermedadRepository.save(enfermedad);
        return ResponseEntity.ok(enfermedad);
    }


}
