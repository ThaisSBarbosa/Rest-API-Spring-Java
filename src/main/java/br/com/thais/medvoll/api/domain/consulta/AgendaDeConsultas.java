package br.com.thais.medvoll.api.domain.consulta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.thais.medvoll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import br.com.thais.medvoll.api.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
import br.com.thais.medvoll.api.domain.medico.Medico;
import br.com.thais.medvoll.api.domain.medico.MedicoRepository;
import br.com.thais.medvoll.api.domain.paciente.PacienteRepository;
import br.com.thais.medvoll.api.infra.exception.ValidacaoException;
import jakarta.validation.Valid;

@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores;
    // o spring cria uma lista de todos os validadores que implementam essa interface
    
    @Autowired
    private List<ValidadorCancelamentoDeConsulta> validadoresCancelamento;

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {
        if (!pacienteRepository.existsById(dados.idPaciente())) {
            throw new ValidacaoException("Id do paciente informado não existe!");
        }

        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
            throw new ValidacaoException("Id do médico informado não existe!");
        }
        
        validadores.forEach(v -> v.valida(dados));

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var medico = escolherMedico(dados);
        var consulta = new Consulta(null, medico, paciente, dados.data(), null);
        consultaRepository.save(consulta);
        
        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() != null) {
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if (dados.especialidade() == null) {
            throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido!");
        }
        
        var medicos = medicoRepository.escolherMedicoLivreNaData(dados.especialidade(), dados.data(), PageRequest.of(0, 10)).toList();

        List<Medico> listaModificavel = new ArrayList<>(medicos);
        Collections.shuffle(listaModificavel);
        
        if (listaModificavel.size() == 0) {
        	throw new ValidacaoException("Não há médico disponível!");
        }
        
        return listaModificavel.get(0);
    }

    public void cancelar(DadosCancelamentoConsulta dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        validadoresCancelamento.forEach(v -> v.valida(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }
}
