package com.saludvital.mssaludvital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medicamentos",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "nombre")
       })
public class Medicamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @OneToMany(mappedBy = "medicamento", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ItemReceta> itemRecetas = new ArrayList<>();

    public Medicamento() {}

    public Medicamento(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<ItemReceta> getItemRecetas() {
        return itemRecetas;
    }

    public void setItemRecetas(List<ItemReceta> itemRecetas) {
        this.itemRecetas = itemRecetas;
    }
}