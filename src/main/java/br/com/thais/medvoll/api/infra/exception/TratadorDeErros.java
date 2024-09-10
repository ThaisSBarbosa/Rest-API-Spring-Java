package br.com.thais.medvoll.api.infra.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class TratadorDeErros {
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity tratarErro404() {
		return ResponseEntity.notFound().build();
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity tratarErro400(MethodArgumentNotValidException e) {
		var erros = e.getFieldErrors();
		var errosDTO = erros.stream().map(DadosErroValidacao::new).toList();
		return ResponseEntity.badRequest().body(errosDTO);
	}
	
	public record DadosErroValidacao(String campo, String msg) {
		public DadosErroValidacao(FieldError error) {
			this(error.getField(), error.getDefaultMessage());
		}
	}
}
