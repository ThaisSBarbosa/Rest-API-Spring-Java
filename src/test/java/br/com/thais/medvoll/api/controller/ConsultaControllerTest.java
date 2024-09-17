package br.com.thais.medvoll.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.thais.medvoll.api.TestComunsMethods;
import br.com.thais.medvoll.api.domain.consulta.DadosAgendamentoConsulta;
import br.com.thais.medvoll.api.domain.consulta.DadosDetalhamentoConsulta;
import br.com.thais.medvoll.api.domain.medico.Especialidade;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

	@Autowired
	private EntityManager em;
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	@DisplayName("Deveria retornar código 400 quando o body estiver vazio.")
	@WithMockUser
	void agendarCenario1() throws Exception {
		var response = mvc.perform(post("/consultas"))
					      .andReturn()
					      .getResponse();
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	@DisplayName("Deveria retornar código 200 quando o body estiver correto.")
	@WithMockUser
	@Transactional
	void agendarCenario2() throws Exception {
		var data = LocalDateTime.now().plusDays(1);
		var especialidade = Especialidade.CARDIOLOGIA;
		
		var medico = TestComunsMethods.cadastrarMedico(em, "Medico", "med@voll.com", "123456", especialidade);
		var paciente = TestComunsMethods.cadastrarPaciente(em, "Paciente", "paciente@voll.com", "00000000000");		
		var dadosAgendamento = new DadosAgendamentoConsulta(medico.getId(), paciente.getId(), data, especialidade);
		
		var dadosDetalhamentoEsperado = new DadosDetalhamentoConsulta(null, medico.getId(), paciente.getId(), data);
		
		var response = mvc.perform(post("/consultas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(dadosAgendamentoConsultaJson.write(dadosAgendamento).getJson()))
					      .andReturn()
					      .getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
	    DadosDetalhamentoConsulta dadosResposta = objectMapper.readValue(
	        response.getContentAsString(), DadosDetalhamentoConsulta.class);
		
		assertThat(dadosResposta)
		    .extracting(DadosDetalhamentoConsulta::idMedico, DadosDetalhamentoConsulta::idPaciente, DadosDetalhamentoConsulta::data)
		    .containsExactly(dadosDetalhamentoEsperado.idMedico(), dadosDetalhamentoEsperado.idPaciente(), dadosDetalhamentoEsperado.data());
	}

// Usando o mockito
//	@Test
//	@DisplayName("Deveria retornar código 200 quando o body estiver correto.")
//	@WithMockUser
//	void agendarCenario2() throws Exception {
//		var data = LocalDateTime.now().plusHours(2);
//		var especialidade = Especialidade.CARDIOLOGIA;
//		
//		var dadosAgendamento = new DadosAgendamentoConsulta(2l, 3l, data, especialidade);
//		var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 2l, 3l, data);
//		
//		when(agendaDeConsultas.agendar(argThat(dados -> 
//				dados.idMedico().equals(dadosAgendamento.idMedico()) &&
//				dados.idPaciente().equals(dadosAgendamento.idPaciente()) &&
//				dados.data().equals(dadosAgendamento.data()) &&
//				dados.especialidade().equals(dadosAgendamento.especialidade()))))
//			.thenReturn(dadosDetalhamento);
//		
//		var response = mvc.perform(post("/consultas")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(dadosAgendamentoConsultaJson.write(dadosAgendamento).getJson()))
//					      .andReturn()
//					      .getResponse();
//		
//		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//		
//		var jsonEsperado = dadosDetalhamentoConsultaJson.write(
//					dadosDetalhamento).getJson();
//		
//		assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
//	}
}
