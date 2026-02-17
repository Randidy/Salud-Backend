package com.saludvital.mssaludvital.service;

import com.saludvital.mssaludvital.entity.Cita;
import com.saludvital.mssaludvital.entity.HorarioAtencion;
import com.saludvital.mssaludvital.entity.Medico;
import com.saludvital.mssaludvital.entity.User;
import com.saludvital.mssaludvital.enums.DiaSemana;
import com.saludvital.mssaludvital.enums.Especialidad;
import com.saludvital.mssaludvital.enums.EstadoDoctor;
import com.saludvital.mssaludvital.repository.CitaRepository;
import com.saludvital.mssaludvital.repository.MedicoRepository;
import com.saludvital.mssaludvital.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CitaRepository citaRepository;

    //ANDREA
    public Optional<Medico> findByUsuarioId(Long usuarioId) {
        return medicoRepository.findByUserId(usuarioId);
    }
    
    

    public List<Medico> findMedicosDisponiblesPorFechaYHora(LocalDate fecha, LocalTime hora) {
        List<Medico> medicosActivos = medicoRepository.findMedicosDisponibles(EstadoDoctor.ACTIVO);

        DiaSemana diaSemana = DiaSemana.fromString(fecha.getDayOfWeek().name());

        return medicosActivos.stream()
                .filter(medico -> medico.getHorarios().stream().anyMatch(horario ->
                        horario.getDia().equals(diaSemana) &&
                        !hora.isBefore(horario.getHoraInicio()) &&
                        !hora.isAfter(horario.getHoraFin())
                ))
                .filter(medico -> citaRepository.findByMedicoAndFechaAndHora(medico.getId(), fecha, hora).isEmpty())
                .collect(Collectors.toList());
    } 
   
    public List<Medico> findAll() {
        return medicoRepository.findByEstado(EstadoDoctor.ACTIVO);
    }

    
    
    public Optional<Medico> findById(Long id) {
        return medicoRepository.findById(id);
    }

    public List<Medico> findMedicosDisponibles() {
        return medicoRepository.findByEstado(EstadoDoctor.ACTIVO);
    }

    public Optional<Medico> findByNumeroLicencia(String numeroLicencia) {
        return medicoRepository.findByNumeroLicencia(numeroLicencia);
    }

    public boolean existsByNumeroLicencia(String numeroLicencia) {
        return medicoRepository.existsByNumeroLicencia(numeroLicencia);
    }

   
    public Medico save(Medico medico) {
        return medicoRepository.save(medico);
    }

    @Transactional
    public void deleteById(Long id) {
        Medico medico = medicoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + id));

        medico.setEstado(EstadoDoctor.INACTIVO);
        medico.setDisponible(false);

        if (medico.getHorarios() != null) {
            medico.getHorarios().forEach(h -> h.setActivo(false));
        }

        medicoRepository.saveAndFlush(medico);
    }
   
    public Medico createMedico(String nombre, String apellido, String numeroLicencia,
                               String telefono, String email, Especialidad especialidad,
                               BigDecimal tarifaConsulta, User usuario, List<HorarioAtencion> horarios) {

        if (existsByNumeroLicencia(numeroLicencia)) {
            throw new RuntimeException("Ya existe un médico con el número de licencia: " + numeroLicencia);
        }

        Medico medico = new Medico(nombre, apellido, numeroLicencia, especialidad, tarifaConsulta);
        medico.setTelefono(telefono);
        medico.setEmail(email);
        medico.setUsuario(usuario);    
        medico.setEstado(EstadoDoctor.ACTIVO);
        medico.setDisponible(true);

        if (horarios != null && !horarios.isEmpty()) {
            horarios.forEach(h -> h.setMedico(medico));
            medico.setHorarios(horarios);
        }

        return save(medico);
    }

    
    
    
    public Medico updateMedico(Long id, String nombre, String apellido, String numeroLicencia,
                               String telefono, String email, Especialidad especialidad,
                               BigDecimal tarifaConsulta, EstadoDoctor estado, Boolean disponible,
                               List<HorarioAtencion> horarios) {

        Medico medico = findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + id));

        if (!medico.getNumeroLicencia().equals(numeroLicencia) && existsByNumeroLicencia(numeroLicencia)) {
            throw new RuntimeException("Ya existe un médico con el número de licencia: " + numeroLicencia);
        }

        medico.setNombre(nombre);
        medico.setApellido(apellido);
        medico.setNumeroLicencia(numeroLicencia);
        medico.setTelefono(telefono);
        medico.setEmail(email);
        medico.setEspecialidad(especialidad);
        medico.setTarifaConsulta(tarifaConsulta);
        medico.setEstado(estado);
        medico.setDisponible(disponible);

        if (horarios != null) {
            medico.getHorarios().clear();
            horarios.forEach(h -> h.setMedico(medico));
            medico.getHorarios().addAll(horarios);
        }

        return save(medico);
    }
}
