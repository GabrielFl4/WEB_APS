package br.edu.unifaj.poo.eu_paciente.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponse {
    private String status;
    private String nome;
    private Long id;
}
