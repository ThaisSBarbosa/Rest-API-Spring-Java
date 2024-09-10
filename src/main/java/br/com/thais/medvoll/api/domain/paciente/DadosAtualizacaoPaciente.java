package br.com.thais.medvoll.api.domain.paciente;

import br.com.thais.medvoll.api.domain.endereco.DadosEndereco;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoPaciente(
		@NotNull
		Long id, 
		String nome, 
		String telefone, 
		DadosEndereco endereco) {
}
