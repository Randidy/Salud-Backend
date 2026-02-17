package com.saludvital.mssaludvital.repository;

import com.saludvital.mssaludvital.entity.HorarioAtencion;
import com.saludvital.mssaludvital.entity.Medico;
import com.saludvital.mssaludvital.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencion, Long> {
    List<HorarioAtencion> findByMedico(Medico medico);
    List<HorarioAtencion> findByMedicoAndDia(Medico medico, DiaSemana dia);
    
    @Query("SELECT h FROM HorarioAtencion h WHERE h.medico.id = :medicoId")
    List<HorarioAtencion> findByMedicoId(@Param("medicoId") Long medicoId);
    
    @Query("SELECT h FROM HorarioAtencion h WHERE h.medico.id = :medicoId AND h.dia = :dia")
    List<HorarioAtencion> findByMedicoIdAndDia(@Param("medicoId") Long medicoId, @Param("dia") DiaSemana dia);
}