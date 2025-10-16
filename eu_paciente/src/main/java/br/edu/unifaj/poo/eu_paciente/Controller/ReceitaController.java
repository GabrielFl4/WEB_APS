package br.edu.unifaj.poo.eu_paciente.Controller;

import br.edu.unifaj.poo.eu_paciente.Model.Receita;
import br.edu.unifaj.poo.eu_paciente.Service.ReceitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Receita> criarReceita(@Valid @RequestBody Receita receita){

        Receita receitaSalva = receitaService.criar(receita);
    return ResponseEntity.ok(receitaSalva);
    }

    @GetMapping
    public ResponseEntity<List<Receita>> listarTodasAsReceitas() {
        List<Receita> receitas = receitaService.listarTodas();
        return ResponseEntity.ok(receitas);
    }
}
