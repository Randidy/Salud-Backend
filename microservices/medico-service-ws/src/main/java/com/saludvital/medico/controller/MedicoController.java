package com.saludvital.medico.controller;

import com.saludvital.medico.repository.MedicoRepository;
import com.saludvital.mssaludvital.entity.Medico;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {
    private final MedicoRepository repository;

    public MedicoController(MedicoRepository repository) { this.repository = repository; }

    @GetMapping
    public List<Medico> findAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public Medico findById(@PathVariable Long id) { return repository.findById(id).orElseThrow(); }

    @PostMapping
    public Medico save(@RequestBody Medico entity) { return repository.save(entity); }
}
