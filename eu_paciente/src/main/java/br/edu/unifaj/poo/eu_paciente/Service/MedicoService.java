package br.edu.unifaj.poo.eu_paciente.Service;

import br.edu.unifaj.poo.eu_paciente.DTO.LoginRequest;
import br.edu.unifaj.poo.eu_paciente.Model.Medico;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicoService {

    public List<Medico> listaDeMedicos = new ArrayList<>();

    //todo, Médico implantado manualmente
    public MedicoService() {
        Medico medicoTeste = new Medico(1L, "Dr. João Silva", "joao.silva@email.com", "1234", "Cardiologista");
        this.listaDeMedicos.add(medicoTeste);
    }

    public boolean verificaLogin(LoginRequest loginRequest){

        for (Medico medico: listaDeMedicos){

            boolean emailCorresponde = medico.getEmail().equals(loginRequest.email());

            if (emailCorresponde) {
                boolean senhaCorresponde = medico.getSenha().equals(loginRequest.senha());

                if (senhaCorresponde) {
                    System.out.println("Login bem-sucedido para: " + medico.getNome());
                    return true;
                }
            }
        }

        System.out.println("Falha no login para o email: " + loginRequest.email());
        return false;
    }

}
