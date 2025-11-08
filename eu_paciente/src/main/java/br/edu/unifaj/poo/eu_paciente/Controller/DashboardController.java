package br.edu.unifaj.poo.eu_paciente.Controller;

import br.edu.unifaj.poo.eu_paciente.DAO.ConsultaDAO;
import br.edu.unifaj.poo.eu_paciente.Service.ConsultaService;
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

    @GetMapping("consultas-hoje/{idMedico}")
    public ResponseEntity<Integer> getTotalConsultasHoje(@PathVariable Long idMedico){

        int totalConsulta = consultaService.obterConsultaDeHoje(idMedico);

        return ResponseEntity.ok(totalConsulta);
    }

}
