package br.com.hioktec.minhasfinancas.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe representa uma resposta de token JWT do servidor.
 * @author rodolfo
 */
@Getter
@Setter
public class JwtResponse {
	
	private String token;
	
	private String tipo = "Portador ";
	
	public JwtResponse(String token) {
		this.token = token;
	}
	
}
