package com.saludvital.paciente.repository;

import com.saludvital.mssaludvital.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {}
