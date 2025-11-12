package br.edu.unifaj.poo.eu_paciente.Controller;

import br.edu.unifaj.poo.eu_paciente.DAO.ConsultaDAO;
import br.edu.unifaj.poo.eu_paciente.Service.ConsultaService;
import br.edu.unifaj.poo.eu_paciente.Service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/resumo")
@CrossOrigin("*")
public class DashboardController {

    @Autowired
    private ConsultaDAO consultaDAO;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private ReceitaService receitaService;

    @GetMapping("consultas-hoje/{idMedico}")
    public ResponseEntity<Integer> getTotalConsultasHoje(@PathVariable Long idMedico){

        int totalConsulta = consultaService.obterConsultaDeHoje(idMedico);

        return ResponseEntity.ok(totalConsulta);
    }

    @GetMapping("receitas-hoje/{idMedico}")
    public ResponseEntity<Integer> getTotalReceitasHoje(@PathVariable Long idMedico) {

        // Chama o novo método do service
        int totalReceitas = receitaService.contarReceitasDeHoje(idMedico);

        // Retorna o número (ex: 3)
        return ResponseEntity.ok(totalReceitas);
    }

}
