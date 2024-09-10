package br.com.thais.medvoll.api.domain.paciente;

import br.com.thais.medvoll.api.domain.endereco.DadosEndereco;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroPaciente(
		@NotBlank
		String nome, 
		@NotBlank
		@Email
		String email, 
		@NotBlank
		String telefone, 
		@NotBlank
		@Pattern(regexp = "\\d{11}")
		String cpf,
		@NotNull
		DadosEndereco endereco) {
}
