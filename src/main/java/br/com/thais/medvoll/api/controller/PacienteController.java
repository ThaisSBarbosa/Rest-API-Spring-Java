package br.com.thais.medvoll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import br.com.thais.medvoll.api.medico.DadosListagemMedico;
import br.com.thais.medvoll.api.paciente.DadosAtualizacaoPaciente;
import br.com.thais.medvoll.api.paciente.DadosCadastroPaciente;
import br.com.thais.medvoll.api.paciente.DadosListagemPaciente;
import br.com.thais.medvoll.api.paciente.Paciente;
import br.com.thais.medvoll.api.paciente.PacienteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
	
	@Autowired
	private PacienteRepository repository;
	
	@PostMapping
	@Transactional
	public void cadastra(@RequestBody @Valid DadosCadastroPaciente dados) {
		System.out.println(dados);
		repository.save(new Paciente(dados));
	}	

	@GetMapping
	public Page<DadosListagemPaciente> lista(@PageableDefault(size = 2, sort = { "nome" }) Pageable paginacao) {
		var pacientes = repository.findAllByAtivoTrue(paginacao);
		return pacientes.map(DadosListagemPaciente::new); // nao precisa stream pq o page ja tem o metodo map
	}
	
	@PutMapping
	@Transactional
	public void atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados) {
		var paciente = repository.getReferenceById(dados.id());
		paciente.atualizarDados(dados);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public void excluir(@PathVariable Long id) {
		//repository.deleteById(id);
		var paciente = repository.getReferenceById(id); //exclusão lógica
		paciente.excluir();
	}
}