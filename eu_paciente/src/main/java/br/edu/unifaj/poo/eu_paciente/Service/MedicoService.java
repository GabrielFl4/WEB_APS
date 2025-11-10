package br.edu.unifaj.poo.eu_paciente.Service;

import br.edu.unifaj.poo.eu_paciente.DAO.MedicoDAO;
import br.edu.unifaj.poo.eu_paciente.DTO.LoginRequest;
import br.edu.unifaj.poo.eu_paciente.Model.Medico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicoService {

    @Autowired
    MedicoDAO dao;

    public List<Medico> listaDeMedicos = new ArrayList<>();

    public Medico verificaLogin(LoginRequest loginRequest) throws Exception {
        Medico medico = dao.getMedicoPorEmail(loginRequest.email());

        if (medico == null){
            System.out.println("Falha no login: Email não encontrado - " + loginRequest.email());
            return null;
        }

        boolean senhaCorresponde = medico.getSenha().equals(loginRequest.senha());

        if (senhaCorresponde){
            System.out.println("Login bem sucedido para - " + loginRequest.email());
            return medico;
        }else{
            System.out.println("Falha no login: Senha incorreta para - " + loginRequest.email());
            return null;
        }

    }

    public List<Medico> buscarMedicos() throws Exception{
        return listaDeMedicos = dao.selectEspecialidades();
    }

}
