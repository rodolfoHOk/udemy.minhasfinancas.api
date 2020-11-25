package br.com.hioktec.minhasfinancas.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioResponse {
	
	private Long id;
	
	private String nome;
	
	private String nomeUsuario;
	
	private Boolean isAdmin; 
}
