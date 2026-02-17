package com.saludvital.mssaludvital.repository;

import com.saludvital.mssaludvital.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    

    @Query("SELECT p FROM Paciente p WHERE p.usuario.email = :email")
    Optional<Paciente> findByUsuarioEmail(@Param("email") String email);

    @Query("SELECT p FROM Paciente p WHERE p.usuario.id = :userId")
    Optional<Paciente> findByUsuarioId(@Param("userId") Long userId);

   

    Optional<Paciente> findByNumeroIdentificacion(String numeroIdentificacion);

    Boolean existsByNumeroIdentificacion(String numeroIdentificacion);

    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.alergias LEFT JOIN FETCH p.enfermedades")
    List<Paciente> findAllWithDetalles();
}
