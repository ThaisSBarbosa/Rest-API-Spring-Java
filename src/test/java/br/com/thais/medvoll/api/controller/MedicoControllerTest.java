package br.com.thais.medvoll.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
import br.com.thais.medvoll.api.domain.endereco.Endereco;
import br.com.thais.medvoll.api.domain.medico.DadosCadastroMedico;
import br.com.thais.medvoll.api.domain.medico.DadosDetalhamentoMedico;
import br.com.thais.medvoll.api.domain.medico.Especialidade;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class MedicoControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private JacksonTester<DadosCadastroMedico> dadosCadastroMedico;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	@DisplayName("Deveria retornar código 400 quando o body estiver vazio.")
	@WithMockUser
	void cadastraCenario1() throws Exception {
		var response = mvc.perform(post("/medicos"))
					      .andReturn()
					      .getResponse();
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	@DisplayName("Deveria retornar código 200 quando o body estiver correto.")
	@WithMockUser
	@Transactional
	void cadastraCenario2() throws Exception {		
		var dadosMedico = TestComunsMethods.dadosMedico("Medico", "med@voll.com", "123456", Especialidade.CARDIOLOGIA);

		var dadosDetalhamentoEsperado = new DadosDetalhamentoMedico(null, 
				dadosMedico.nome(), dadosMedico.email(), dadosMedico.crm(), dadosMedico.telefone(),
				dadosMedico.especialidade(), new Endereco(dadosMedico.endereco()));
		
		var response = mvc.perform(post("/medicos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(dadosCadastroMedico.write(dadosMedico).getJson()))
					      .andReturn()
					      .getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		
	    DadosDetalhamentoMedico dadosResposta = objectMapper.readValue(
	        response.getContentAsString(), DadosDetalhamentoMedico.class);
	    
		assertThat(dadosResposta)
		    .extracting(DadosDetalhamentoMedico::nome, 
		    		    DadosDetalhamentoMedico::email, 
		    		    DadosDetalhamentoMedico::crm,
		    		    DadosDetalhamentoMedico::telefone,
		    		    DadosDetalhamentoMedico::especialidade)
		    .containsExactly(dadosDetalhamentoEsperado.nome(), 
		    		         dadosDetalhamentoEsperado.email(), 
		    		         dadosDetalhamentoEsperado.crm(),
		    		         dadosDetalhamentoEsperado.telefone(),
		    		         dadosDetalhamentoEsperado.especialidade());
		
		assertThat(dadosResposta.endereco())
		    .extracting(Endereco::getLogradouro, 
		    			Endereco::getBairro, 
		    			Endereco::getCep,
		    			Endereco::getCidade,
		    			Endereco::getUf,
		    			Endereco::getComplemento,
		    			Endereco::getNumero)
		    .containsExactly(dadosDetalhamentoEsperado.endereco().getLogradouro(), 
		    		         dadosDetalhamentoEsperado.endereco().getBairro(), 
		    		         dadosDetalhamentoEsperado.endereco().getCep(),
		    		         dadosDetalhamentoEsperado.endereco().getCidade(),
		    		         dadosDetalhamentoEsperado.endereco().getUf(),
		    		         dadosDetalhamentoEsperado.endereco().getComplemento(),
		    		         dadosDetalhamentoEsperado.endereco().getNumero());
	}


}
