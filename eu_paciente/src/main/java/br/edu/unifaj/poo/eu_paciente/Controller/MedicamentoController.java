package br.edu.unifaj.poo.eu_paciente.Controller;

import br.edu.unifaj.poo.eu_paciente.Model.Medicamento;
import br.edu.unifaj.poo.eu_paciente.Service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/medicamentos")
@CrossOrigin("*")
public class MedicamentoController {

    @Autowired
    ReceitaService receitaService;

    @GetMapping("/{id_receita}")
    public ResponseEntity<List<Medicamento>> getMedicamentos(@PathVariable Long id_receita) throws Exception{
        List<Medicamento> medicamentos = receitaService.exibirMedicamentos(id_receita);
        return ResponseEntity.ok(medicamentos);
    }
}
