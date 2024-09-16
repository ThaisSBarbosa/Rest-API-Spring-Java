package br.com.thais.medvoll.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.thais.medvoll.api.domain.medico.DadosAtualizacaoMedico;
import br.com.thais.medvoll.api.domain.medico.DadosCadastroMedico;
import br.com.thais.medvoll.api.domain.medico.DadosDetalhamentoMedico;
import br.com.thais.medvoll.api.domain.medico.DadosListagemMedico;
import br.com.thais.medvoll.api.domain.medico.Medico;
import br.com.thais.medvoll.api.domain.medico.MedicoRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

	@Autowired
	private MedicoRepository repository;

	@PostMapping
	@Transactional
	public ResponseEntity cadastra(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
		var medico = new Medico(dados);
		repository.save(medico);
		var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
		return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
	}

	@GetMapping
	public ResponseEntity<Page<DadosListagemMedico>> lista(@PageableDefault(size = 2, sort = { "nome" }) Pageable paginacao) {
		var medicos = repository.findAllByAtivoTrue(paginacao);
		var page = medicos.map(DadosListagemMedico::new); // nao precisa stream pq o page ja tem o metodo map
		return ResponseEntity.ok(page);
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity atualiza(@RequestBody @Valid DadosAtualizacaoMedico dados) {
		var medico = repository.getReferenceById(dados.id());
		medico.atualizarDados(dados);
		
		return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity exclui(@PathVariable Long id) {
		//repository.deleteById(id);
		var medico = repository.getReferenceById(id); //exclusão lógica
		medico.excluir();
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	@Transactional
	public ResponseEntity detalha(@PathVariable Long id) {
		var medico = repository.getReferenceById(id);
		return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
	}
}
