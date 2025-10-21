package br.edu.unifaj.poo.eu_paciente.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import br.edu.unifaj.poo.eu_paciente.Service.ConsultaService;
import br.edu.unifaj.poo.eu_paciente.DTO.AtualizarStatusDTO;
import br.edu.unifaj.poo.eu_paciente.Model.Consulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@CrossOrigin("*")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/medico/{medicoId}/hoje")
    public ResponseEntity<List<Consulta>> getConsultasDeHoje(@PathVariable Long medicoId) {
        List<Consulta> consultas = consultaService.buscarConsultaPorDia(medicoId);
        return ResponseEntity.ok(consultas);
    }

    @PutMapping("/{consultaId}/status")
    public ResponseEntity<Consulta> updateStatus(
            @PathVariable Long consultaId,
            @RequestBody AtualizarStatusDTO dto) {

        Consulta consultaAtualizada = consultaService.atualizarStatusConsulta(consultaId, dto.status());

        if (consultaAtualizada != null) {
            return ResponseEntity.ok(consultaAtualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}