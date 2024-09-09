package br.com.thais.medvoll.api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thais.medvoll.api.medico.DadosAtualizacaoMedico;
import br.com.thais.medvoll.api.medico.DadosCadastroMedico;
import br.com.thais.medvoll.api.medico.DadosListagemMedico;
import br.com.thais.medvoll.api.medico.Medico;
import br.com.thais.medvoll.api.medico.MedicoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

	@Autowired
	private MedicoRepository repository;

	@PostMapping
	@Transactional
	public void cadastra(@RequestBody @Valid DadosCadastroMedico dados) {
		repository.save(new Medico(dados));
	}

	@GetMapping
	public Page<DadosListagemMedico> lista(@PageableDefault(size = 2, sort = { "nome" }) Pageable paginacao) {
		var medicos = repository.findAllByAtivoTrue(paginacao);
		return medicos.map(DadosListagemMedico::new); // nao precisa stream pq o page ja tem o metodo map
	}
	
	@PutMapping
	@Transactional
	public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
		var medico = repository.getReferenceById(dados.id());
		medico.atualizarDados(dados);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public void excluir(@PathVariable Long id) {
		//repository.deleteById(id);
		var medico = repository.getReferenceById(id); //exclusão lógica
		medico.excluir();
	}
}
