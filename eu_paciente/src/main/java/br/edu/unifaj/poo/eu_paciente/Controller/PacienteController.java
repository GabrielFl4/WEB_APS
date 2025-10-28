package br.edu.unifaj.poo.eu_paciente.Controller;

import br.edu.unifaj.poo.eu_paciente.DTO.PacienteRequest;
import br.edu.unifaj.poo.eu_paciente.DTO.PacienteResponse;
import br.edu.unifaj.poo.eu_paciente.DAO.PacienteDao;
import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import br.edu.unifaj.poo.eu_paciente.Service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/pacientes")
@CrossOrigin("*")
public class PacienteController {

    @Autowired
    PacienteDao dao;

    @Autowired
    private PacienteService pacienteService;

    @PostMapping("/login")
    public PacienteResponse fazerLogin(@RequestBody PacienteRequest request){
        Optional<Paciente> p = dao.loginPaciente(request.getEmail(), request.getSenha());
        PacienteResponse resp = new PacienteResponse();
        if (p.isEmpty()){
            resp.setStatus("401");
        } else {
            resp.setStatus("200");
            resp.setId(p.get().getId());
            resp.setNome(p.get().getNome());
        }
        return resp;
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Paciente> buscarPorCpf(@PathVariable String cpf){
        Paciente paciente = pacienteService.BuscarCpf(cpf);

        if (paciente != null){
            return ResponseEntity.ok(paciente);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
