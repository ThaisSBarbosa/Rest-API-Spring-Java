package br.com.thais.medvoll.api;

import java.time.LocalDateTime;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import br.com.thais.medvoll.api.domain.consulta.Consulta;
import br.com.thais.medvoll.api.domain.endereco.DadosEndereco;
import br.com.thais.medvoll.api.domain.medico.DadosCadastroMedico;
import br.com.thais.medvoll.api.domain.medico.Especialidade;
import br.com.thais.medvoll.api.domain.medico.Medico;
import br.com.thais.medvoll.api.domain.paciente.DadosCadastroPaciente;
import br.com.thais.medvoll.api.domain.paciente.Paciente;
import jakarta.persistence.EntityManager;

public class TestComunsMethods {
	public static void cadastrarConsulta(EntityManager em, Medico medico, Paciente paciente, LocalDateTime data) {
        em.persist(new Consulta(null, medico, paciente, data, null));
    }

	public static Medico cadastrarMedico(EntityManager em, String nome, String email, String crm, Especialidade especialidade) {
        var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
        em.persist(medico);
        return medico;
    }

	public static Paciente cadastrarPaciente(EntityManager em, String nome, String email, String cpf) {
        var paciente = new Paciente(dadosPaciente(nome, email, cpf));
        em.persist(paciente);
        return paciente;
    }

	public static DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade) {
        return new DadosCadastroMedico(
                nome,
                email,
                "61999999999",
                crm,
                especialidade,
                dadosEndereco()
        );
    }

    private static DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf) {
        return new DadosCadastroPaciente(
                nome,
                email,
                "61999999999",
                cpf,
                dadosEndereco()
        );
    }

    private static DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }
}
