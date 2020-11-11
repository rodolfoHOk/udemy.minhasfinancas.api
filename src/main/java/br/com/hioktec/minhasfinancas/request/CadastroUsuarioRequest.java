package br.com.hioktec.minhasfinancas.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe representa uma requisição de cadastro de usuário (com validação)
 * @author rodolfo
 */
@Getter
@Setter
public class CadastroUsuarioRequest {
	
	@NotBlank
	@Size(min = 4, max = 40)
	private String nome;
	
	@NotBlank
	@Size(min = 3, max = 15)
	private String nomeUsuario;
	
	@NotBlank
	@Size(max = 40)
	@Email
	private String email;
	
	@NotBlank
	@Size(min = 6, max = 20)
	private String senha;
	
	private String autoridade;
	
}
