package br.edu.unifaj.poo.eu_paciente.Controller;

import br.edu.unifaj.poo.eu_paciente.DTO.PacienteRequest;
import br.edu.unifaj.poo.eu_paciente.DTO.PacienteResponse;
import br.edu.unifaj.poo.eu_paciente.DAO.PacienteDAO;
import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import br.edu.unifaj.poo.eu_paciente.Service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/pacientes")
@CrossOrigin("*")
public class PacienteController {

    @Autowired
    PacienteService service;

    @Autowired
    private PacienteService pacienteService;

    @PostMapping("/login")
    public PacienteResponse fazerLogin(@RequestBody PacienteRequest request) throws Exception {
        Optional<Paciente> p = service.loginPaciente(request.getEmail(), request.getSenha());
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

    @GetMapping("/{idPaciente}")
    public ResponseEntity<Paciente> buscarPaciente(@PathVariable Long idPaciente) throws Exception{
        Paciente p;
        p = service.buscarPacienteId(idPaciente);
        return ResponseEntity.ok(p);
    }

    @PutMapping("/{idPaciente}")
    public ResponseEntity<?> atualizarPaciente(@PathVariable Long idPaciente, @RequestBody Map<String, String> body) throws Exception {

        String telefone = body.get("telefone");
        String complemento = body.get("complemento");

        if (complemento != null && complemento.length() > 250) {
            return ResponseEntity.badRequest().body("Complemento deve ter no máximo 250 caracteres"); // Uso badRequest para enviar que tá errado o tamanho
        }

        int funcionou = service.atualizarPaciente(idPaciente, telefone, complemento);
        if (funcionou == 0) {
            return ResponseEntity.status(404).body("Paciente não encontrado");
        }

        Paciente atualizado = service.buscarPacienteId(idPaciente); // Busco novamente o paciente para mandar
        return ResponseEntity.ok(atualizado);
    }


    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Paciente> buscarPorCpf(@PathVariable String cpf) throws Exception {
        Paciente paciente = pacienteService.BuscarCpf(cpf);

        if (paciente != null){
            return ResponseEntity.ok(paciente);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ficha/{idPaciente}")
    public ResponseEntity<Paciente> getFichaMedica(@PathVariable Long idPaciente) {
        try {
            Paciente fichaCompleta = pacienteService.buscarFichaCompleta(idPaciente);
            return ResponseEntity.ok(fichaCompleta);
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {

            System.err.println("Erro ao buscar ficha completa: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
