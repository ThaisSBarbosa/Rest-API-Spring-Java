package br.com.thais.medvoll.api.domain.consulta.validacoes.cancelamento;

import br.com.thais.medvoll.api.domain.consulta.DadosCancelamentoConsulta;

public interface ValidadorCancelamentoDeConsulta {
    void valida(DadosCancelamentoConsulta dados);
}
