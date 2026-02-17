package com.saludvital.mssaludvital.repository;

import com.saludvital.mssaludvital.entity.Alergia;
import com.saludvital.mssaludvital.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlergiaRepository extends JpaRepository<Alergia, Long> {
    List<Alergia> findByPaciente(Paciente paciente);
    
    @Query("SELECT a FROM Alergia a WHERE a.paciente.id = :pacienteId")
    List<Alergia> findByPacienteId(@Param("pacienteId") Long pacienteId);
}