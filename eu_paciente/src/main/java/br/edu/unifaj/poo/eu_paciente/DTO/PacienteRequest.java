package br.edu.unifaj.poo.eu_paciente.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacienteRequest {
    private String email = "email";
    private String senha = "senha";
}
