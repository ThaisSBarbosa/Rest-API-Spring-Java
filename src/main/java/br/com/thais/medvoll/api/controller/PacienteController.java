package br.com.thais.medvoll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.thais.medvoll.api.domain.paciente.DadosAtualizacaoPaciente;
import br.com.thais.medvoll.api.domain.paciente.DadosCadastroPaciente;
import br.com.thais.medvoll.api.domain.paciente.DadosDetalhamentoPaciente;
import br.com.thais.medvoll.api.domain.paciente.DadosListagemPaciente;
import br.com.thais.medvoll.api.domain.paciente.Paciente;
import br.com.thais.medvoll.api.domain.paciente.PacienteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
	
	@Autowired
	private PacienteRepository repository;
	
	@PostMapping
	@Transactional
	public ResponseEntity cadastra(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder) {
		var paciente = new Paciente(dados);
		repository.save(paciente);
		var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
		return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
	}	

	@GetMapping
	public ResponseEntity<Page<DadosListagemPaciente>> lista(@PageableDefault(size = 2, sort = { "nome" }) Pageable paginacao) {
		var pacientes = repository.findAllByAtivoTrue(paginacao);
		var page = pacientes.map(DadosListagemPaciente::new); // nao precisa stream pq o page ja tem o metodo map
		return ResponseEntity.ok(page);
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity atualiza(@RequestBody @Valid DadosAtualizacaoPaciente dados) {
		var paciente = repository.getReferenceById(dados.id());
		paciente.atualizarDados(dados);
		
		return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity exclui(@PathVariable Long id) {
		//repository.deleteById(id);
		var paciente = repository.getReferenceById(id); //exclusão lógica
		paciente.excluir();
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	@Transactional
	public ResponseEntity detalha(@PathVariable Long id) {
		var paciente = repository.getReferenceById(id);
		return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
	}
}