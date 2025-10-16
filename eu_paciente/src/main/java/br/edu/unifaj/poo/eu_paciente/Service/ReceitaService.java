package br.edu.unifaj.poo.eu_paciente.Service;

import br.edu.unifaj.poo.eu_paciente.Model.Receita;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReceitaService {

    //todo, Tabela de Banco de Dados simulada e o incremento de ID
    private final List<Receita> listaDeReceitas = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    public Receita criar(Receita novaReceita){

        long novoId = contadorId.incrementAndGet();
        novaReceita.setIdReceita(novoId);

        listaDeReceitas.add(novaReceita);
        System.out.println("Nova Receita salva: " + novaReceita.getNomePaciente() + "-" + novaReceita.getNomeMedicamento());
        return novaReceita;
    }

    public List<Receita> listarTodas() {
        return listaDeReceitas;
    }
}
