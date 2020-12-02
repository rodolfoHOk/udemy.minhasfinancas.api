package br.com.hioktec.minhasfinancas.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe que representa uma requisição de atualização de usuario
 * @author rodolfo
 */
@Getter
@Setter
public class AtualizarUsuarioRequest {
	
	@NotNull
	private Long id;

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
	
	@NotBlank
	@Size(max = 20)
	private String autoridade;
}
