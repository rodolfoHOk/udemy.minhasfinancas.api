package br.com.hioktec.minhasfinancas.request;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe que representa uma requisição de atualização de um lançamento
 * @author rodolfo
 */
@Getter
@Setter
public class AtualizarLancamentoRequest {
	
	@NotNull
	private Long id;
	
	@NotBlank
	@Size(max = 255)
	private String descricao;
	
	@NotNull
	@Min(value = 1)
	@Max(value = 12)
	private Integer mes;
	
	@NotNull
	@Positive
	@Digits(integer = 4, fraction = 0)
	private Integer ano;
	
	@NotNull
	@DecimalMin(value = "0.01", inclusive = true)
	@Digits(integer = 16, fraction = 2)
	private BigDecimal valor;
	
	@NotBlank
	@Size(max = 20)
	private String tipo;
	
	@NotBlank
	@Size(max = 20)
	private String status;
	
	@NotNull
	private Long usuario;
}

