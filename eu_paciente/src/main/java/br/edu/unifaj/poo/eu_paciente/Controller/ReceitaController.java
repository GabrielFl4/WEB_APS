package br.edu.unifaj.poo.eu_paciente.Controller;

import br.edu.unifaj.poo.eu_paciente.DTO.ReceitaDTO;
import br.edu.unifaj.poo.eu_paciente.Model.Medico;
import br.edu.unifaj.poo.eu_paciente.Model.Receita;
import br.edu.unifaj.poo.eu_paciente.Service.ReceitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/receitas")
@CrossOrigin("*")
public class ReceitaController {

    @Autowired
    ReceitaService receitaService;

    @GetMapping("/{id_paciente}")
    public ResponseEntity<List<Receita>> getReceitas(@PathVariable Long id_paciente) throws Exception{
        List<Receita> receitas = receitaService.exibirReceitas(id_paciente);
        for (Receita r : receitas){
            r.setMedicamentos(receitaService.exibirMedicamentos(r.getId()));
        }

        return ResponseEntity.ok(receitas);
    }


    @PostMapping
    public ResponseEntity<Receita> criarReceita(@RequestBody Receita receita) throws Exception{
        try {
            Receita receitaSalva = receitaService.criar(receita);
            return ResponseEntity.status(HttpStatus.CREATED).body(receitaSalva);

        } catch (Exception e) {
            System.err.println("Erro ao criar receita: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<Receita>> listarReceitasDoMedico(@PathVariable Long idMedico) {
        try {
            List<Receita> receitas = receitaService.listarPorMedico(idMedico);
            return ResponseEntity.ok(receitas);
        } catch (Exception e) {
            System.err.println("Erro ao listar receitas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
