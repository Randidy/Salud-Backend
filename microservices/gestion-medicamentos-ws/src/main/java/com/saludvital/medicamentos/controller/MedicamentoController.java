package com.saludvital.medicamentos.controller;

import com.saludvital.medicamentos.repository.MedicamentoRepository;
import com.saludvital.mssaludvital.entity.Medicamento;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {
    private final MedicamentoRepository repository;

    public MedicamentoController(MedicamentoRepository repository) { this.repository = repository; }

    @GetMapping
    public List<Medicamento> findAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public Medicamento findById(@PathVariable Long id) { return repository.findById(id).orElseThrow(); }

    @PostMapping
    public Medicamento save(@RequestBody Medicamento entity) { return repository.save(entity); }
}
