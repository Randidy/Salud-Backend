package com.saludvital.mssaludvital.repository;

import com.saludvital.mssaludvital.entity.Medico;
import com.saludvital.mssaludvital.enums.Especialidad;
import com.saludvital.mssaludvital.enums.EstadoDoctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByNumeroLicencia(String numeroLicencia);

    Boolean existsByNumeroLicencia(String numeroLicencia);

    List<Medico> findByEspecialidad(Especialidad especialidad);

    List<Medico> findByEstado(EstadoDoctor estado);


    List<Medico> findByEspecialidadAndEstado(Especialidad especialidad, EstadoDoctor estado);
    
    
    @Query("SELECT m FROM Medico m WHERE m.usuario.email = :email")
    Optional<Medico> findByUsuarioEmail(@Param("email") String email);

    @Query("SELECT m FROM Medico m WHERE m.usuario.id = :userId")
    Optional<Medico> findByUsuarioId(@Param("userId") Long userId);

    @Query("SELECT m FROM Medico m WHERE m.estado = :estado")
    List<Medico> findMedicosDisponibles(@Param("estado") EstadoDoctor estado);
    
    
    @Query("SELECT m FROM Medico m WHERE m.usuario.id = :userId")
    Optional<Medico> findByUserId(@Param("userId") Long userId);
    
    Optional<Medico> findByUsuario_Id(Long usuarioId);

}
