package com.saludvital.mssaludvital.repository;

import com.saludvital.mssaludvital.entity.Enfermedad;
import com.saludvital.mssaludvital.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnfermedadRepository extends JpaRepository<Enfermedad, Long> {
    List<Enfermedad> findByPaciente(Paciente paciente);
    
    @Query("SELECT e FROM Enfermedad e WHERE e.paciente.id = :pacienteId")
    List<Enfermedad> findByPacienteId(@Param("pacienteId") Long pacienteId);
}