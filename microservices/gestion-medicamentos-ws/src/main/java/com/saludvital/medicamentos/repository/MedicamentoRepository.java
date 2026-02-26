package com.saludvital.medicamentos.repository;

import com.saludvital.mssaludvital.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {}
