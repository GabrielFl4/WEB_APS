package br.edu.unifaj.poo.eu_paciente.Controller;

import br.edu.unifaj.poo.eu_paciente.DTO.LoginRequest;
import br.edu.unifaj.poo.eu_paciente.Model.Medico;
import br.edu.unifaj.poo.eu_paciente.Service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/medicos")
@CrossOrigin("*")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @PostMapping("/login")
    public ResponseEntity<String> acessarLogin(@RequestBody LoginRequest loginRequest) throws Exception {

        boolean loginValido = medicoService.verificaLogin(loginRequest);

        if (loginValido){
            return ResponseEntity.ok("Login bem-sucedido!");
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos.");
        }

    }

    @GetMapping("")
    public ResponseEntity<List<Medico>> getMedicos() throws Exception{
        List<Medico> medicos = medicoService.buscarMedicos();
        return ResponseEntity.ok(medicos);
    }
}
