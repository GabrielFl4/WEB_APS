package br.edu.unifaj.poo.eu_paciente.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import br.edu.unifaj.poo.eu_paciente.Service.ConsultaService;
import br.edu.unifaj.poo.eu_paciente.DTO.AtualizarStatusDTO;
import br.edu.unifaj.poo.eu_paciente.Model.Consulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@CrossOrigin("*")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/medico/{medicoId}/hoje")
    public ResponseEntity<List<Consulta>> getConsultasDeHoje(@PathVariable Long medicoId) throws Exception {
        List<Consulta> consultas = consultaService.buscarConsultaPorDia(medicoId);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Consulta>> getConsultas(@PathVariable Long idUsuario) throws Exception {
        List<Consulta> consultas = consultaService.exibirConsultas(idUsuario);
        return ResponseEntity.ok(consultas);
    }

    @PutMapping("/{consultaId}/status")
    public ResponseEntity<Consulta> updateStatus(@PathVariable Long consultaId, @RequestBody AtualizarStatusDTO dto) throws Exception {

        Consulta consultaAtualizada = consultaService.atualizarStatusConsulta(consultaId, dto.status());

        if (consultaAtualizada != null) {
            return ResponseEntity.ok(consultaAtualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{consultaId}/pago")
    public ResponseEntity<Consulta> updatePago(@PathVariable Long consultaId, @RequestBody boolean pago) throws Exception{
        Consulta atualizada = consultaService.atualizarPagoConsulta(consultaId, pago);

        System.out.println("FINANCEIRO |Consulta de ID " + atualizada.getId() + " - Status de pagamento alterado para: " + atualizada.isConsultaPaga());

        return ResponseEntity.ok(atualizada);
    }

    @PostMapping("/")
    public ResponseEntity<Consulta> adicionarConsulta(@RequestBody Consulta c) throws Exception {
        try {
            Consulta consulta = consultaService.addConsulta(c);
            return ResponseEntity.ok(consulta);
        } catch (Exception ex) {
            if (chaveDuplicada(ex)){
                return ResponseEntity.status(409).body(null); // Se erro de duplicado
            }
            return ResponseEntity.status(500).body(null); // Se qualquer outra bomba de erro
        }
    }

    // Utilitário que passa o erro do sql
    private boolean chaveDuplicada(Throwable throwable) {
        while (throwable != null) {
            if (throwable instanceof SQLException se) {
                String state = se.getSQLState();
                int code = se.getErrorCode();
                if ("23505".equals(state)) return true;  // Erro quando duplicado
            }
            throwable = throwable.getCause();
        }
        return false;
    }
}