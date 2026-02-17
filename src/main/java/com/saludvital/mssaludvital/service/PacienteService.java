package com.saludvital.mssaludvital.service;

import com.saludvital.mssaludvital.dto.SignUpRequest;
import com.saludvital.mssaludvital.entity.Alergia;
import com.saludvital.mssaludvital.entity.Enfermedad;
import com.saludvital.mssaludvital.entity.Paciente;
import com.saludvital.mssaludvital.entity.User;
import com.saludvital.mssaludvital.repository.PacienteRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Transactional
    public Paciente actualizarPacienteCompleto(Paciente paciente, SignUpRequest request) {
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        paciente.setNumeroIdentificacion(request.getNumeroIdentificacion());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setTelefono(request.getTelefono());
        paciente.setDireccion(request.getDireccion());

        if (paciente.getAlergias() != null) {
            paciente.getAlergias().clear();
        }
        if (request.getAlergias() != null) {
            Set<Alergia> alergias = request.getAlergias().stream()
                    .map(nombre -> new Alergia(nombre, "", paciente))
                    .collect(Collectors.toSet());
            paciente.getAlergias().addAll(alergias);
        }

        if (paciente.getEnfermedades() != null) {
            paciente.getEnfermedades().clear();
        }
        if (request.getEnfermedades() != null) {
            Set<Enfermedad> enfermedades = request.getEnfermedades().stream()
                    .map(nombre -> new Enfermedad(nombre, "", paciente))
                    .collect(Collectors.toSet());
            paciente.getEnfermedades().addAll(enfermedades);
        }

        return pacienteRepository.save(paciente);
    }

    public Optional<Paciente> findById(Long id) {
        return pacienteRepository.findById(id);
    }

    public Optional<Paciente> findByNumeroIdentificacion(String numeroIdentificacion) {
        return pacienteRepository.findByNumeroIdentificacion(numeroIdentificacion);
    }

    public Optional<Paciente> findByUsuarioEmail(String email) {
        return pacienteRepository.findByUsuarioEmail(email);
    }

    public Optional<Paciente> findByUsuarioId(Long userId) {
        return pacienteRepository.findByUsuarioId(userId);
    }

    public boolean existsByNumeroIdentificacion(String numeroIdentificacion) {
        return pacienteRepository.existsByNumeroIdentificacion(numeroIdentificacion);
    }

    public Paciente save(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public void deleteById(Long id) {
        pacienteRepository.deleteById(id);
    }

    public java.util.List<Paciente> findAllWithDetalles() {
        return pacienteRepository.findAllWithDetalles();
    }

    
    
    @Transactional
    public Paciente createPaciente(String nombre,
                                   String apellido,
                                   String numeroIdentificacion,
                                   java.time.LocalDate fechaNacimiento,
                                   User usuario,
                                   Set<Alergia> alergias,
                                   Set<Enfermedad> enfermedades,
                                   String telefono,
                                   String direccion) {

        Paciente paciente = new Paciente(nombre, apellido, numeroIdentificacion, fechaNacimiento, telefono, direccion);
        paciente.setUsuario(usuario);

        if (alergias != null && !alergias.isEmpty()) {
            alergias.forEach(a -> a.setPaciente(paciente));
            paciente.setAlergias(alergias);
        }

        if (enfermedades != null && !enfermedades.isEmpty()) {
            enfermedades.forEach(e -> e.setPaciente(paciente));
            paciente.setEnfermedades(enfermedades);
        }

        return pacienteRepository.save(paciente);
    }

  
    
    

    // ===================== ACTUALIZAR PACIENTE BÁSICO =====================
    public Paciente updatePaciente(Long id,
                                   String nombre,
                                   String numeroIdentificacion,
                                   LocalDate fechaNacimiento) {

        Paciente paciente = findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));

        if (!paciente.getNumeroIdentificacion().equals(numeroIdentificacion)
                && existsByNumeroIdentificacion(numeroIdentificacion)) {
            throw new RuntimeException("Ya existe un paciente con el número de identificación: " + numeroIdentificacion);
        }

        paciente.setNombre(nombre);
        paciente.setNumeroIdentificacion(numeroIdentificacion);
        paciente.setFechaNacimiento(fechaNacimiento);

        return save(paciente);
    }
}
