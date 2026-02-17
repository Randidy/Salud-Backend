package com.saludvital.mssaludvital.service;

import com.saludvital.mssaludvital.entity.Cita;
import com.saludvital.mssaludvital.entity.Medico;
import com.saludvital.mssaludvital.entity.Paciente;
import com.saludvital.mssaludvital.enums.DiaSemana;
import com.saludvital.mssaludvital.enums.EstadoCita;
import com.saludvital.mssaludvital.enums.EstadoDoctor;
import com.saludvital.mssaludvital.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

 
    public List<Cita> findByPacienteId(Long pacienteId) {
        return citaRepository.findByPacienteIdOrderByFechaDesc(pacienteId)
                .stream()
                .filter(c -> c.getMedico() != null && c.getMedico().getEstado() == EstadoDoctor.ACTIVO)
                .toList();
    }

    public Cita crearCita(Paciente paciente, Medico medico, LocalDate fecha, LocalTime hora, String motivo) {
        validarCita(paciente, medico, fecha, hora);

        Cita cita = new Cita(fecha, hora, paciente, medico, motivo, medico.getTarifaConsulta());
        cita.setEstado(EstadoCita.PROGRAMADA);

        return citaRepository.save(cita);
    }

    
    public List<Cita> findByMedicoId(Long medicoId) {
        return citaRepository.findByMedicoIdOrderByFechaDesc(medicoId)
                .stream()
                .filter(c -> c.getMedico() != null && c.getMedico().getEstado() == EstadoDoctor.ACTIVO)
                .toList();
    }
    
    

    public Cita completarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada con id: " + id));
        cita.setEstado(EstadoCita.COMPLETADA);
        return citaRepository.save(cita);
    }

  
    public List<Cita> findAll() {
        return citaRepository.findAll()
                .stream()
                .filter(c -> c.getMedico() != null && c.getMedico().getEstado() == EstadoDoctor.ACTIVO)
                .toList();
    }

    public Cita actualizarCita(Long id, LocalDate fecha, LocalTime hora, String motivo) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada con id: " + id));

        LocalDateTime fechaHoraCita = LocalDateTime.of(fecha, hora);
        if (fechaHoraCita.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pueden actualizar citas en el pasado");
        }

        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setMotivo(motivo);

        return citaRepository.save(cita);
    }

    public Cita cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada con id: " + id));
        cita.setEstado(EstadoCita.CANCELADA);
        return citaRepository.save(cita);
    }

   
    private void validarCita(Paciente paciente, Medico medico, LocalDate fecha, LocalTime hora) {
        DiaSemana diaSemana = DiaSemana.fromString(fecha.getDayOfWeek().name());
        boolean atiendeEseDia = medico.getHorarios().stream().anyMatch(h ->
                h.getDia().equals(diaSemana) &&
                !hora.isBefore(h.getHoraInicio()) &&
                !hora.isAfter(h.getHoraFin())
        );
        if (!atiendeEseDia) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El médico no atiende en ese día y hora");
        }

        Long citasDelDia = citaRepository.countCitasPacienteEnFecha(paciente.getId(), fecha);
        if (citasDelDia >= 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pueden programar más de 3 citas en el mismo día");
        }

        if (existeCita(medico, fecha, hora)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El horario seleccionado ya está ocupado");
        }

        if (LocalDateTime.of(fecha, hora).isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pueden programar citas en el pasado");
        }
    }

  
    public boolean existeCita(Medico medico, LocalDate fecha, LocalTime hora) {
        List<Cita> citasConflicto = citaRepository.findByMedicoAndFechaAndHora(medico.getId(), fecha, hora);
        return !citasConflicto.isEmpty();
    }

    
    public void eliminarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada con id: " + id));

        citaRepository.delete(cita);
    }
}
