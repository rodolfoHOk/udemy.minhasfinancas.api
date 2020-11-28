package br.com.hioktec.minhasfinancas.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe que representa uma requisição de atualização do status de um lançamento
 * @author rodolfo
 */
@Getter
@Setter
public class AtualizarStatusRequest {
	
	@NotBlank
	@Size(max = 20)
	private String status;
}
