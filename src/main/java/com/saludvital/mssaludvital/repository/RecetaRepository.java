package com.saludvital.mssaludvital.repository;

import com.saludvital.mssaludvital.entity.Receta;
import com.saludvital.mssaludvital.entity.Medico;
import com.saludvital.mssaludvital.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    List<Receta> findByPaciente(Paciente paciente);
    List<Receta> findByMedico(Medico medico);
    
    @Query("SELECT r FROM Receta r WHERE r.paciente.id = :pacienteId ORDER BY r.fechaEmision DESC")
    List<Receta> findByPacienteIdOrderByFechaEmisionDesc(@Param("pacienteId") Long pacienteId);
    
    @Query("SELECT r FROM Receta r WHERE r.medico.id = :medicoId ORDER BY r.fechaEmision DESC")
    List<Receta> findByMedicoIdOrderByFechaEmisionDesc(@Param("medicoId") Long medicoId);
    
    @Query("SELECT r FROM Receta r WHERE r.fechaCaducidad < CURRENT_DATE")
    List<Receta> findRecetasCaducadas();
}