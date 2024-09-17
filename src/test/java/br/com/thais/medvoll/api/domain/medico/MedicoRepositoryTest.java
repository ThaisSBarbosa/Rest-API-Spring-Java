package br.com.thais.medvoll.api.domain.medico;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import br.com.thais.medvoll.api.TestComunsMethods;
import br.com.thais.medvoll.api.domain.consulta.Consulta;
import br.com.thais.medvoll.api.domain.endereco.DadosEndereco;
import br.com.thais.medvoll.api.domain.paciente.DadosCadastroPaciente;
import br.com.thais.medvoll.api.domain.paciente.Paciente;
import jakarta.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {
	
	@Autowired
	private MedicoRepository repository;
	
	@Autowired
	private EntityManager em;

	@Test
	@DisplayName("Deveria devolver null quando o médico cadastrado não está disponível na data.")
	void escolherMedicoLivreNaDataCenario1() {
		// given ou arrange
		var proximaSegundaAs10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
		
		var medico = TestComunsMethods.cadastrarMedico(em, "Medico", "med@voll.com", "123456", Especialidade.CARDIOLOGIA);
		var paciente = TestComunsMethods.cadastrarPaciente(em, "Paciente", "paciente@voll.com", "00000000000");
		TestComunsMethods.cadastrarConsulta(em, medico, paciente, proximaSegundaAs10);	
		
		// when ou act
		var medicosLivres = repository.escolherMedicoLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10, PageRequest.of(0, 10)).toList();
		
		// then ou assert
		assertThat(medicosLivres.size()).isEqualTo(0);
	}
	
	@Test
	@DisplayName("Deveria devolver o médico cadastrado quando ele está disponível na data.")
	void escolherMedicoLivreNaDataCenario2() {
		// given ou arrange
		var proximaSegundaAs11 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(11, 0);
		
		TestComunsMethods.cadastrarMedico(em, "Medico 2", "med2@voll.com", "123477", Especialidade.ORTOPEDIA);
		
		// when ou act
		var medicosLivres = repository.escolherMedicoLivreNaData(Especialidade.ORTOPEDIA, proximaSegundaAs11, PageRequest.of(0, 10)).toList();
		
		// then ou assert
		assertThat(medicosLivres.size()).isEqualTo(1);
	}
}
