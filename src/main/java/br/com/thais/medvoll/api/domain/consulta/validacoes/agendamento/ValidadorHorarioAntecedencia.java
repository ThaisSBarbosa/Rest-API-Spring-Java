package br.com.thais.medvoll.api.domain.consulta.validacoes.agendamento;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import br.com.thais.medvoll.api.domain.consulta.DadosAgendamentoConsulta;
import br.com.thais.medvoll.api.infra.exception.ValidacaoException;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoDeConsulta{
	public void valida(DadosAgendamentoConsulta dados) {
		var dataConsulta = dados.data();
		
		if (Duration.between(LocalDateTime.now(), dataConsulta).toMinutes() < 30) {
			throw new ValidacaoException("Consulta deve ser agendada com antecedência mínima de 30 minutos.");
		}
	}
}
