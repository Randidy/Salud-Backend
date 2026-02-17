package com.saludvital.mssaludvital.repository;

import com.saludvital.mssaludvital.entity.ItemReceta;
import com.saludvital.mssaludvital.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRecetaRepository extends JpaRepository<ItemReceta, Long> {
    List<ItemReceta> findByReceta(Receta receta);
    
    @Query("SELECT i FROM ItemReceta i WHERE i.receta.id = :recetaId")
    List<ItemReceta> findByRecetaId(@Param("recetaId") Long recetaId);
}