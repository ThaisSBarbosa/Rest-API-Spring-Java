package br.com.thais.medvoll.api.domain.consulta.validacoes;

import br.com.thais.medvoll.api.domain.consulta.DadosAgendamentoConsulta;

public interface ValidadorAgendamentoDeConsulta {
    void validar(DadosAgendamentoConsulta dados);
}
