package br.com.thais.medvoll.api.domain.medico;

import br.com.thais.medvoll.api.domain.endereco.DadosEndereco;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoMedico(
		@NotNull
		Long id, 
		String nome, 
		String telefone, 
		DadosEndereco endereco) {
}
