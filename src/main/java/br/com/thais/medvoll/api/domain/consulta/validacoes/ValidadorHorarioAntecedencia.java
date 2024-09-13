package br.com.thais.medvoll.api.domain.consulta.validacoes;

import java.time.Duration;
import java.time.LocalDate;

import br.com.thais.medvoll.api.domain.ValidacaoException;
import br.com.thais.medvoll.api.domain.consulta.DadosAgendamentoConsulta;

public class ValidadorHorarioAntecedencia {
	public void valida(DadosAgendamentoConsulta dados) {
		var dataConsulta = dados.data();
		
		if (Duration.between(LocalDate.now(), dataConsulta).toMinutes() < 30) {
			throw new ValidacaoException("Consulta deve ser agendada com antecedência mínima de 30 minutos.");
		}
	}
}
