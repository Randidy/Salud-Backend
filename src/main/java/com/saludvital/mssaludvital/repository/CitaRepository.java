package com.saludvital.mssaludvital.repository;

import com.saludvital.mssaludvital.entity.Cita;
import com.saludvital.mssaludvital.entity.Medico;
import com.saludvital.mssaludvital.entity.Paciente;
import com.saludvital.mssaludvital.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByPaciente(Paciente paciente);
    List<Cita> findByMedico(Medico medico);
    List<Cita> findByEstado(EstadoCita estado);

    @Query("SELECT c FROM Cita c WHERE c.paciente.id = :pacienteId AND c.estado = :estado")
    List<Cita> findByPacienteIdAndEstado(@Param("pacienteId") Long pacienteId, @Param("estado") EstadoCita estado);

    @Query("SELECT c FROM Cita c WHERE c.medico.id = :medicoId AND c.estado = :estado")
    List<Cita> findByMedicoIdAndEstado(@Param("medicoId") Long medicoId, @Param("estado") EstadoCita estado);

    @Query("SELECT c FROM Cita c WHERE c.medico.id = :medicoId ORDER BY c.fecha DESC, c.hora DESC")
    List<Cita> findByMedicoIdOrderByFechaDesc(@Param("medicoId") Long medicoId);

   
    @Query("SELECT c FROM Cita c WHERE c.paciente.id = :pacienteId ORDER BY c.fecha DESC, c.hora DESC")
    List<Cita> findByPacienteIdOrderByFechaDesc(@Param("pacienteId") Long pacienteId);

    @Query("SELECT COUNT(c) FROM Cita c WHERE c.paciente.id = :pacienteId AND c.fecha = :fecha AND c.estado != 'CANCELADA'")
    Long countCitasPacienteEnFecha(@Param("pacienteId") Long pacienteId, @Param("fecha") LocalDate fecha);

    @Query("SELECT c FROM Cita c WHERE c.medico.id = :medicoId AND c.fecha = :fecha AND c.hora = :hora")
    List<Cita> findByMedicoAndFechaAndHora(@Param("medicoId") Long medicoId,
                                           @Param("fecha") LocalDate fecha,
                                           @Param("hora") LocalTime hora);

    @Query("SELECT c FROM Cita c WHERE c.medico.id = :medicoId AND c.fecha = :fecha")
    List<Cita> findByMedicoIdAndFecha(@Param("medicoId") Long medicoId, @Param("fecha") LocalDate fecha);
}
