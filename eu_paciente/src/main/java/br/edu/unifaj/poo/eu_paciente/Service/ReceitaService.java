package br.edu.unifaj.poo.eu_paciente.Service;

import br.edu.unifaj.poo.eu_paciente.DTO.ReceitaDTO;
import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import br.edu.unifaj.poo.eu_paciente.Model.Receita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReceitaService {

    //todo, Tabela de Banco de Dados simulada e o incremento de ID
    private final List<Receita> listaDeReceitas = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    @Autowired
    private PacienteService pacienteService;

    public Receita criar(ReceitaDTO receitaDTO) throws Exception {

        // Primeiro passo é verificar se o Paciente existe
        Paciente pacienteEncontrado = pacienteService.buscarPorId(receitaDTO.getPacienteId());

        if (pacienteEncontrado == null) {
            throw new RuntimeException("Paciente não encontrado com o ID: " + receitaDTO.getPacienteId());
        }

        // criação de um novo objeto 'Receita'
        Receita novaReceita = new Receita(
                pacienteEncontrado,
                receitaDTO.getNomeMedicamento(),
                receitaDTO.getDescricaoUsoMedicamento()
        );

        // Vinculando um novo 'Id' e adicionando na lista
        novaReceita.setIdReceita(contadorId.incrementAndGet());
        listaDeReceitas.add(novaReceita);

        System.out.println("Nova Receita salva para: " + pacienteEncontrado.getNome());

        return novaReceita;

    }

    public List<Receita> listarTodas() {
        return listaDeReceitas;
    }
}
