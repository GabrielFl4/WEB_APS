package br.edu.unifaj.poo.eu_paciente.Controller;

import br.edu.unifaj.poo.eu_paciente.DTO.ReceitaDTO;
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
    private ReceitaService receitaService;

    @PostMapping
    public ResponseEntity<Receita> criarReceita(@RequestBody ReceitaDTO receitaDTO) throws Exception{
        try {
            Receita receitaSalva = receitaService.criar(receitaDTO);
            return ResponseEntity.ok(receitaSalva);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Receita>> listarTodasAsReceitas() {
        List<Receita> receitas = receitaService.listarTodas();
        return ResponseEntity.ok(receitas);
    }
}
