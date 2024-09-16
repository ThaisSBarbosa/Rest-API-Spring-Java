package br.com.thais.medvoll.api.domain.consulta.validacoes.agendamento;

import br.com.thais.medvoll.api.domain.consulta.DadosAgendamentoConsulta;

public interface ValidadorAgendamentoDeConsulta {
    void valida(DadosAgendamentoConsulta dados);
}
