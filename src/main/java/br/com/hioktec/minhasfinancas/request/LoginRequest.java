package br.com.hioktec.minhasfinancas.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe que representa uma requisição de Login (com validação)
 * @author rodolfo
 */
@Getter
@Setter
public class LoginRequest {
	
	@NotBlank
	@Size(min = 4, max = 40)
	private String nomeUsuarioOuEmail;
	
	@NotBlank
	@Size(min = 6, max = 20)
	private String senha;
	
}
