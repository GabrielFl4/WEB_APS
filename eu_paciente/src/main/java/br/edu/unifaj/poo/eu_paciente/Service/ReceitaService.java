package br.edu.unifaj.poo.eu_paciente.Service;

import br.edu.unifaj.poo.eu_paciente.DAO.ReceitaDAO;
import br.edu.unifaj.poo.eu_paciente.DTO.ReceitaDTO;
import br.edu.unifaj.poo.eu_paciente.Model.Medicamento;
import br.edu.unifaj.poo.eu_paciente.Model.Paciente;
import br.edu.unifaj.poo.eu_paciente.Model.Receita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReceitaService {

    @Autowired
    ReceitaDAO dao;

    @Autowired
    PacienteService pacienteService;

    private List<Receita> listaDeReceitas = new ArrayList<>();
    private List<Medicamento> listaDeMedicamentos = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong();

    public List<Receita> exibirReceitas(Long id_paciente) throws Exception{
        listaDeReceitas = dao.receitaPorPaciente(id_paciente);
        return listaDeReceitas;
    }

    public List<Medicamento> exibirMedicamentos(Long id_receita) throws Exception{
        listaDeMedicamentos = dao.medicamentosPorReceita(id_receita);
        return listaDeMedicamentos;
    }

    public int contarReceitasDeHoje(Long idMedico) {
        LocalDate hoje = LocalDate.now();
        return dao.contarReceitasPorDia(idMedico, hoje);
    }

    @Transactional
    public Receita criar(Receita receita) throws Exception{

    receita.setData(LocalDate.now());

    Long novoIdReceita = dao.adicionarReceita(receita);
    receita.setId(novoIdReceita);

    if (receita.getMedicamentos() != null && !receita.getMedicamentos().isEmpty()){
        for (Medicamento med : receita.getMedicamentos()){
            med.setId_receita(novoIdReceita.intValue());
            dao.adicionarMedicamento(med);
        }
    }
    return receita;
    }

    public List<Receita> listarPorMedico(Long idMedico) throws Exception {
        List<Receita> receitas = dao.receitasPorMedico(idMedico);

        for (Receita r : receitas) {
            r.setMedicamentos(dao.medicamentosPorReceita(r.getId()));
        }

        return receitas;
    }

}
