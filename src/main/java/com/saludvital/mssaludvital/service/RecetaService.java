package com.saludvital.mssaludvital.service;

import com.saludvital.mssaludvital.entity.*;
import com.saludvital.mssaludvital.enums.EstadoDoctor;
import com.saludvital.mssaludvital.repository.RecetaRepository;
import com.saludvital.mssaludvital.repository.ItemRecetaRepository;
import com.saludvital.mssaludvital.repository.AlergiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RecetaService {
    
    @Autowired
    private RecetaRepository recetaRepository;
    
    @Autowired
    private ItemRecetaRepository itemRecetaRepository;
    
    @Autowired
    private AlergiaRepository alergiaRepository;

   
    public List<Receta> findAll() {
        return recetaRepository.findAll(); 
    }

    public List<Receta> findByPacienteId(Long pacienteId) {
        return recetaRepository.findByPacienteIdOrderByFechaEmisionDesc(pacienteId);
    }

    public List<Receta> findByMedicoId(Long medicoId) {
        return recetaRepository.findByMedicoIdOrderByFechaEmisionDesc(medicoId);
    }
    
    public Optional<Receta> findById(Long id) {
        return recetaRepository.findById(id);
    }

    

    public List<Receta> findRecetasCaducadas() {
        return recetaRepository.findRecetasCaducadas();
    }

    @Transactional
    public Receta save(Receta receta) {
        return recetaRepository.save(receta);
    }

    public void deleteById(Long id) {
        recetaRepository.deleteById(id);
    }

    @Transactional
    public Receta crearReceta(Paciente paciente, Medico medico, List<ItemRecetaRequest> itemsRequest) {
        List<Alergia> alergiasPaciente = alergiaRepository.findByPacienteId(paciente.getId());
        for (ItemRecetaRequest itemRequest : itemsRequest) {
            String nombreMedicamento = itemRequest.getMedicamento().getNombre();
            boolean esAlergico = alergiasPaciente.stream()
                    .anyMatch(alergia -> alergia.getNombre().equalsIgnoreCase(nombreMedicamento));
            
            if (esAlergico) {
                throw new RuntimeException("No se puede recetar " + nombreMedicamento + 
                                         " porque el paciente es alérgico a este medicamento");
            }
        }
        
   
        LocalDate fechaEmision = LocalDate.now();
        LocalDate fechaCaducidad = fechaEmision.plusDays(30); 
        
        Receta receta = new Receta(fechaEmision, fechaCaducidad, paciente, medico);
        receta = save(receta);
        
        for (ItemRecetaRequest itemRequest : itemsRequest) {
            ItemReceta item = new ItemReceta(receta, itemRequest.getMedicamento(), 
                                           itemRequest.getDosis(), itemRequest.getFrecuencia());
            itemRecetaRepository.save(item);
        }
        
        return receta;
    }

    public static class ItemRecetaRequest {
        private Medicamento medicamento;
        private String dosis;
        private String frecuencia;
        
        public ItemRecetaRequest() {}
        
        public ItemRecetaRequest(Medicamento medicamento, String dosis, String frecuencia) {
            this.medicamento = medicamento;
            this.dosis = dosis;
            this.frecuencia = frecuencia;
        }
        
        public Medicamento getMedicamento() {
            return medicamento;
        }
        
        public void setMedicamento(Medicamento medicamento) {
            this.medicamento = medicamento;
        }
        
        public String getDosis() {
            return dosis;
        }
        
        public void setDosis(String dosis) {
            this.dosis = dosis;
        }
        
        public String getFrecuencia() {
            return frecuencia;
        }
        
        public void setFrecuencia(String frecuencia) {
            this.frecuencia = frecuencia;
        }
    }
}