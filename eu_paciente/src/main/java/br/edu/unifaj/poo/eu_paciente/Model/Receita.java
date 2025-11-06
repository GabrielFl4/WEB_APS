package br.edu.unifaj.poo.eu_paciente.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Receita {

    @Id
    private Long id;

    @Column("data")
    private LocalDate data;

    @Column("id_paciente")
    private int id_paciente;

    @Column("id_medico")
    private int id_medico;

    private Paciente paciente;
    private Medico medico;

    private List<Medicamento> medicamentos = new ArrayList<>();


    public Receita(){
    }

    public Receita(int id_medico, int id_paciente, LocalDate data, Long id, Paciente paciente, Medico medico) {
        this.id_medico = id_medico;
        this.id_paciente = id_paciente;
        this.data = data;
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public int getId_medico() {
        return id_medico;
    }

    public void setId_medico(int id_medico) {
        this.id_medico = id_medico;
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(int id_paciente) {
        this.id_paciente = id_paciente;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }
}
