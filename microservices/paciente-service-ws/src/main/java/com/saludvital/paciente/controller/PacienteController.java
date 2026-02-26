package com.saludvital.paciente.controller;

import com.saludvital.paciente.repository.PacienteRepository;
import com.saludvital.mssaludvital.entity.Paciente;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {
    private final PacienteRepository repository;

    public PacienteController(PacienteRepository repository) { this.repository = repository; }

    @GetMapping
    public List<Paciente> findAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public Paciente findById(@PathVariable Long id) { return repository.findById(id).orElseThrow(); }

    @PostMapping
    public Paciente save(@RequestBody Paciente entity) { return repository.save(entity); }
}
