package br.edu.unifaj.poo.eu_paciente.Controller;

import br.edu.unifaj.poo.eu_paciente.DTO.PacienteRequest;
import br.edu.unifaj.poo.eu_paciente.DTO.PacienteResponse;
import br.edu.unifaj.poo.eu_paciente.Dao.PacienteDao;
import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/paciente")
@CrossOrigin("*")
public class PacienteController {

    @Autowired
    PacienteDao dao;

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
}
