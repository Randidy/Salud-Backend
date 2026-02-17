package com.saludvital.mssaludvital.service;

import com.saludvital.mssaludvital.entity.Medicamento;
import com.saludvital.mssaludvital.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoService {
    
    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public List<Medicamento> findAll() {
        return medicamentoRepository.findAll();
    }

    public Optional<Medicamento> findById(Long id) {
        return medicamentoRepository.findById(id);
    }

    public Optional<Medicamento> findByNombre(String nombre) {
        return medicamentoRepository.findByNombre(nombre);
    }

    public List<Medicamento> findByNombreContaining(String nombre) {
        return medicamentoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public boolean existsByNombre(String nombre) {
        return medicamentoRepository.existsByNombre(nombre);
    }

    public Medicamento save(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    public void deleteById(Long id) {
        medicamentoRepository.deleteById(id);
    }

    public Medicamento crearMedicamento(String nombre, String descripcion) {
        if (existsByNombre(nombre)) {
            throw new RuntimeException("Ya existe un medicamento con el nombre: " + nombre);
        }
        
        Medicamento medicamento = new Medicamento(nombre, descripcion);
        return save(medicamento);
    }

    public Medicamento actualizarMedicamento(Long id, String nombre, String descripcion) {
        Medicamento medicamento = findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con id: " + id));
        
        if (!medicamento.getNombre().equals(nombre) && existsByNombre(nombre)) {
            throw new RuntimeException("Ya existe un medicamento con el nombre: " + nombre);
        }
        
        medicamento.setNombre(nombre);
        medicamento.setDescripcion(descripcion);
        
        return save(medicamento);
    }
}